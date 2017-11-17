package it.smartcommunitylab.mobilityservice.services.test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import it.sayservice.platform.smartplanner.data.message.StopId;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;
import it.smartcommunitylab.mobilityservice.services.service.tobike.TobikeService;
import it.smartcommunitylab.mobilityservice.services.service.tobike.model.TobikeStation;

public class Tobike2Smartplanner {

	private ObjectMapper mapper = new ObjectMapper();
	private TobikeService service = new TobikeService();
	private Map<String, Object> params = Maps.newTreeMap();
	private Map<String, String> input = Maps.newTreeMap();

	public Tobike2Smartplanner() {
		params.put("user", "rovereto");
		params.put("password", "");

		input.put("5100", "Lavis");
		input.put("5093", "Mezzocorona");
		input.put("5092", "Mezzolombardo");
		input.put("5094", "San Michele all'Adige");
	}

	@Test
	public void export() throws Exception {
		for (Entry<String, String> entries : input.entrySet()) {
			String code = entries.getKey();
			String town = entries.getValue();
			
			exportTown(town, code);
		}
	}

	private void exportTown(String town, String code) throws Exception {
		params.put("code", code);

		service.setParameters(params);
		List<MobilityServiceObject> stations = service.invokeService();
		List<ExtendedBikeStation> result = Lists.newArrayList();
		for (MobilityServiceObject obj : stations) {
			TobikeStation station = (TobikeStation) obj;
			result.add(convert(station, town));
		}

		System.err.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
		System.err.println("----------------------");
		
		String name = town.toLowerCase().replaceAll("\\W", "_");
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File("./src/test/resources/results/bike/bs-bike_sharing_" + name + ".json"), result);
		
	}

	private ExtendedBikeStation convert(TobikeStation station, String town) {
		ExtendedBikeStation bs = new ExtendedBikeStation();
		bs.setId(station.getNome() + " - " + town);
		bs.setType("BIKE-RENTAL");
		StopId stop = new StopId();
		stop.setId(bs.getId());
		stop.setAgencyId("BIKE_SHARING_TOBIKE_" + town.toUpperCase().replaceAll("\\W", "_"));
		bs.setStationId(stop);
		bs.setFullName(station.getIndirizzo());
		bs.setAvailableSharingVehicles(station.getStalli());
		bs.setPosts(station.getStalli());
		bs.setMonitored(true);
		bs.setPosition(new double[] { station.getLatitude(), station.getLongitude() });
		return bs;
	}

}

