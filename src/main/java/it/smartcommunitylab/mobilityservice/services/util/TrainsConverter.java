package it.smartcommunitylab.mobilityservice.services.util;

import it.sayservice.platform.smartplanner.data.message.Position;
import it.sayservice.platform.smartplanner.data.message.TType;
import it.sayservice.platform.smartplanner.data.message.Transport;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertDelay;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertType;
import it.sayservice.platform.smartplanner.data.message.alerts.CreatorType;
import it.smartcommunitylab.mobilityservice.services.service.oraritreni.model.PartArr;
import it.smartcommunitylab.mobilityservice.services.service.oraritreni.model.PartenzeArrivi;
import it.smartcommunitylab.mobilityservice.services.service.trentomale.model.TrentoMaleTrain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class TrainsConverter {

	private static final String TNBDG = "6";
	private static final String BZVR = "5";
	private static final String TM = "10";
	private static final String PARTBV = "BRENNERO,BOLZANO".toLowerCase();
	private static final String ARRBV = "ROVERETO,ALA,VERONA PORTA NUOVA, BOLOGNA C.LE,ROMA TERMINI".toLowerCase();
	private static final String PARTTB = "TRENTO".toLowerCase();
	private static final String ARRTB = "BORGO VALSUGANA EST,BASSANO DEL GRAPPA,PADOVA,VENEZIA SANTA LUCIA".toLowerCase();
	private static final String TN_BDG = "TB_R2_G";
	private static final String BDG_TN = "TB_R2_R";
	private static final String BZ_VR = "BV_R1_G";
	private static final String VR_BZ = "BV_R1_R";
	private static final String TM_R = "556";
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

	private static Logger logger = LoggerFactory.getLogger(TrainsConverter.class);

	public static AlertDelay generateDelay(GenericTrain train) {
		AlertDelay delay = new AlertDelay();
		delay.setCreatorId("");
		delay.setCreatorType(CreatorType.SERVICE);
		delay.setDelay(train.getDelay() * (1000 * 60));
		delay.setDescription("");
		delay.setNote("");

		Transport t = new Transport();

		t.setAgencyId(train.getAgencyId());
		t.setRouteId(train.getRouteId());
		t.setRouteShortName(buildRouteLongName(train.getAgencyId(), train.getRouteId()));
		t.setTripId(train.getTripId());
		t.setType(TType.TRAIN);

		delay.setTransport(t);
		delay.setType(AlertType.DELAY);

		Calendar cal = Calendar.getInstance();
		if (train.getRefTime() != null) {
			cal.setTimeInMillis(train.getRefTime());
		}
		Calendar parsed = Calendar.getInstance();
		try {
			parsed.setTime(TIME_FORMAT.parse(train.getTime()));
			cal.set(Calendar.HOUR_OF_DAY, parsed.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, parsed.get(Calendar.MINUTE));
		} catch (Exception e) {
			logger.error("Error parsing delay time: " + e.getMessage());
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 4);
		}
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);

		long from = cal.getTimeInMillis();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		long to = cal.getTimeInMillis();
		delay.setFrom(from);
		delay.setTo(to);
		Position p = new Position();
		p.setName(train.getStation());
		delay.setPosition(p);

		// if (train.getDelay() > 0) {
		// delay.setNote("Train " + train.getNumber() + " has a delay of " +
		// train.getDelay() + " minutes.");
		// } else {
		// delay.setNote("Train " + train.getNumber() + " is on time.");
		// }
		// USE NOTE AS DIRECTION
		delay.setNote(train.getDirection());

		delay.setId(delay.getTransport().getTripId() + "_" + CreatorType.SERVICE + "_" + delay.getFrom() + "_" + delay.getTo());

		return delay;

	}

	public static String buildRouteLongName(String agencyId, String routeId) {
		String res = "";

		if ("5".equals(agencyId)) {
			if (VR_BZ.equals(routeId)) {
				res = "VERONA PORTA NUOVA/BOLZANO";
			} else {
				res = "BOLZANO/VERONA PORTA NUOVA";
			}
		} else if (TNBDG.equals(agencyId)) {
			if (BDG_TN.equals(routeId)) {
				res = "BASSANO DEL GRAPPA/TRENTO";
			} else {
				res = "TRENTO/BASSANO DEL GRAPPA";
			}
		} else if (TM.equals(agencyId)) {
			if (TM_R.equalsIgnoreCase(routeId)) {
				res = "Male/Trento";
			} else {
				res = "Trento/Male";
			}
		}

		return res;
	}

	// public static List<GenericTrain>
	// convertTreni(it.smartcommunitylab.mobilityservice.services.service.oraritreni.model.PartenzeArrivi
	// data) {
	// List<GenericTrain> list = new ArrayList<GenericTrain>();
	// for (PartenzeArrivi pa: data) {
	// try {
	// list = buildTrain(pa);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return list;
	// }

	public static List<GenericTrain> buildTrain(PartenzeArrivi pa) {
		List<GenericTrain> result = new ArrayList<GenericTrain>();

		Map<String, PartArr> partMap = new TreeMap<String, PartArr>();
		Map<String, PartArr> arrMap = new TreeMap<String, PartArr>();
		Multimap<String, String> agencyMap = HashMultimap.create();
		Multimap<String, String> routeMap = HashMultimap.create();
		Multimap<String, String> directionMap = HashMultimap.create();

		for (PartArr part : pa.getPart().getPartenza()) {
			partMap.put(part.getCodtreno(), part);
		}
		for (PartArr arr : pa.getArr().getArrivo()) {
			arrMap.put(arr.getCodtreno(), arr);
		}

		for (String akey : arrMap.keySet()) {
			PartArr arr = arrMap.get(akey);
			String cod = arr.getCodtreno();
			if ("trento".equalsIgnoreCase(pa.getStazione())) {
				if (ARRBV.contains(arr.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, BZVR);
					routeMap.put(cod, VR_BZ);
					directionMap.put(cod, "TRENTO*");
				}
				if (PARTBV.contains(arr.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, BZVR);
					routeMap.put(cod, BZ_VR);
					directionMap.put(cod, "TRENTO*");
				}
				if (ARRTB.contains(arr.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, TNBDG);
					routeMap.put(cod, BDG_TN);
					directionMap.put(cod, "TRENTO*");
				}
				if (PARTTB.contains(arr.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, TNBDG);
					routeMap.put(cod, TN_BDG);
					directionMap.put(cod, "TRENTO*");
				}
			} else if ("bassano del grappa".equalsIgnoreCase(pa.getStazione())) {
				if (ARRBV.contains(arr.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, BZVR);
					agencyMap.put(cod, TNBDG);
					directionMap.put(cod, "TRENTO*");
					routeMap.put(cod, VR_BZ);
					routeMap.put(cod, TN_BDG);
					directionMap.put(cod, "BASSANO DEL GRAPPA*");
				}
				if (PARTBV.contains(arr.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, BZVR);
					agencyMap.put(cod, TNBDG);
					directionMap.put(cod, "TRENTO*");
					routeMap.put(cod, BZ_VR);
					routeMap.put(cod, TN_BDG);
					directionMap.put(cod, "BASSANO DEL GRAPPA*");
				}
				if (ARRTB.contains(arr.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, TNBDG);
					routeMap.put(cod, BDG_TN);
					directionMap.put(cod, "BASSANO DEL GRAPPA*");
				}
				if (PARTTB.contains(arr.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, TNBDG);
					routeMap.put(cod, TN_BDG);
					directionMap.put(cod, "BASSANO DEL GRAPPA*");
				}
			}
		}

		for (String pkey : partMap.keySet()) {
			PartArr part = partMap.get(pkey);
			String cod = part.getCodtreno();
			if ("trento".equalsIgnoreCase(pa.getStazione())) {
				if (ARRBV.contains(part.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, BZVR);
					routeMap.put(cod, BZ_VR);
					directionMap.put(cod, part.getFromOrTo());
				}
				if (PARTBV.contains(part.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, BZVR);
					routeMap.put(cod, VR_BZ);
					directionMap.put(cod, part.getFromOrTo());
				}
				if (ARRTB.contains(part.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, TNBDG);
					routeMap.put(cod, TN_BDG);
					directionMap.put(cod, part.getFromOrTo());
				}
				if (PARTTB.contains(part.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, TNBDG);
					routeMap.put(cod, BDG_TN);
					directionMap.put(cod, part.getFromOrTo());
				}
			} else if ("bassano del grappa".equalsIgnoreCase(pa.getStazione())) {
				if (ARRBV.contains(part.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, BZVR);
					agencyMap.put(cod, TNBDG);
					directionMap.put(cod, part.getFromOrTo());
					routeMap.put(cod, BZ_VR);
					routeMap.put(cod, TN_BDG);
					directionMap.put(cod, part.getFromOrTo());
				}
				if (PARTBV.contains(part.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, BZVR);
					agencyMap.put(cod, TNBDG);
					directionMap.put(cod, part.getFromOrTo());
					routeMap.put(cod, VR_BZ);
					routeMap.put(cod, TN_BDG);
					directionMap.put(cod, part.getFromOrTo());
				}
				if (ARRTB.contains(part.getFromOrTo().toLowerCase())) {
					// agencyMap, cod, TNBDG);
					// routeMap, cod, TN_BDG);
					// directionMap, cod, part.getFromOrTo());
				}
				if (PARTTB.contains(part.getFromOrTo().toLowerCase())) {
					agencyMap.put(cod, TNBDG);
					routeMap.put(cod, BDG_TN);
					directionMap.put(cod, part.getFromOrTo());
				}
			}

		}

		for (String key : agencyMap.keySet()) {
			String from = null;
			String to = null;
			if (partMap.containsKey(key)) {
				to = partMap.get(key).getFromOrTo();
			}
			if (arrMap.containsKey(key)) {
				from = arrMap.get(key).getFromOrTo();
			}
			List<String> ags = Lists.newArrayList(agencyMap.get(key));
			List<String> rts = Lists.newArrayList(routeMap.get(key));
			List<String> drs = Lists.newArrayList(directionMap.get(key));
			PartArr train = null;
			if (partMap.containsKey(key)) {
				train = partMap.get(key);
			} else if (arrMap.containsKey(key)) {
				train = arrMap.get(key);
			}

			String direction = to;
			if (direction == null) {
				direction = pa.getStazione().toUpperCase();
			}

			if (train != null) {
				List<GenericTrain> trains = buildTrains(train, pa.getStazione(), ags, rts, direction);
				result.addAll(trains);
			}
		}

		return result;
	}

	private static List<GenericTrain> buildTrains(PartArr arr, String station, List<String> agencyIds, List<String> routeIds, String direction) {
		List<GenericTrain> result = new ArrayList<GenericTrain>();

		for (int i = 0; i < agencyIds.size(); i++) {
			GenericTrain gt = new GenericTrain();
			long delay = Long.parseLong("0" + arr.getRitardo().replaceAll("\\D", ""));
			gt.setDelay(delay);
			gt.setDirection(direction);
			gt.setStation(station);
			gt.setTime(arr.getOra());
			gt.setId(arr.getCodtreno());
			gt.setAgencyId(agencyIds.get(i));
			gt.setRouteId(routeIds.get(i));
			gt.setTripId(buildTripId(arr.getCodtreno(), (BZVR.equals(agencyIds.get(i)) ? true : false)));
			result.add(gt);
		}

		return result;
	}

	private static String buildTripId(String codTreno, boolean byLength) {
		String res = codTreno.replaceAll(" ", "");
		res = res.replaceAll("REG", "R");
		if (byLength && res.length() == 5) {
			res = res.replaceAll("R", "RV");
		}
		res = res.replaceAll("ES\\*", "ESAV");
		return res;
	}

	public static GenericTrain convertTrentoMale(TrentoMaleTrain t) {
		GenericTrain tmt = new GenericTrain();
		try {
			tmt.setDelay(t.getDelay());
			tmt.setId("" + t.getId());
			tmt.setTripId("" + t.getNumber());
			tmt.setDirection(t.getDirection());
			tmt.setTime(t.getTime());
			tmt.setStation(t.getStation());
			tmt.setAgencyId("10");
			if ("Trento".equalsIgnoreCase(t.getDirection())) {
				tmt.setRouteId("556");
			} else {
				tmt.setRouteId("555");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmt;
	}

}
