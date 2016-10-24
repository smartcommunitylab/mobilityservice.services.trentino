/*******************************************************************************
 * Copyright 2012-2014 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package it.smartcommunitylab.mobilityservice.services.service.tobike;

import it.sayservice.platform.smartplanner.data.message.StopId;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertParking;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertType;
import it.sayservice.platform.smartplanner.data.message.alerts.CreatorType;
import it.smartcommunitylab.mobilityservice.services.MobilityService;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceException;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObjectsContainer;
import it.smartcommunitylab.mobilityservice.services.service.tobike.model.Station;
import it.smartcommunitylab.mobilityservice.services.service.tobike.model.TobikeStation;
import it.smartcommunitylab.mobilityservice.services.service.tobike.ws.TOBikeUtente;
import it.smartcommunitylab.mobilityservice.services.util.ConnectionHelper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mashape.unirest.http.Unirest;

@Component
public class TobikeService extends MobilityService {

	private static final String BIKE_RENTAL = "BIKE-RENTAL";
	private static final String NAME = "name";
	private static final String PLACE = "place";
	private static final String AGENCY_ID = "agencyId";

	private static ObjectMapper mapper = new ObjectMapper();

	@Override
	public List<MobilityServiceObject> invokeService() throws MobilityServiceException {
		try {
			if (parameters == null || parameters.keySet().size() != 3) {
				throw new MobilityServiceException("Needed 3 parameters.");
			}

			List<MobilityServiceObject> result = Lists.newArrayList();

			TOBikeUtente wsc = new TOBikeUtente();
			String s = wsc.getTOBikeUtenteSoap().elencoStazioniPerComuneJSON((String) parameters.get("user"), (String) parameters.get("password"), (String) parameters.get("code"));
			s = s.substring(1, s.length() - 1);
			s = new String(s.getBytes("Windows-1252"), Charsets.UTF_8);

			String stazioni[] = s.split("\\|");

			for (String st : stazioni) {
				String data[] = st.split(";");

				TobikeStation tobikeStation = new TobikeStation();
				tobikeStation.setCodice(data[0]);
				tobikeStation.setNome(data[1]);
				tobikeStation.setIndirizzo(data[2]);
				tobikeStation.setLatitude(Double.parseDouble(data[4]));
				tobikeStation.setLongitude(Double.parseDouble(data[5]));
				String stato = data[6];
				tobikeStation.setStato(stato);
				int posti = stato.replaceAll("[[^0]]", "").length();
				int biciclette = stato.replaceAll("[[^4]]", "").length();
				int stalli = stato.replaceAll("[[^x]]", "").length();
				tobikeStation.setPosti(posti);
				tobikeStation.setBiciclette(biciclette);
				tobikeStation.setStalli(stalli);
				// tobikeStation.setTimestamp(System.currentTimeMillis());

				result.add(tobikeStation);
			}

			return result;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	@Override
	protected int publishData(MobilityServiceObjectsContainer data) throws MobilityServiceException {
		try {
			List<AlertParking> alertList = new ArrayList<AlertParking>();
			List<Station> stations = new ArrayList<Station>();
			for (MobilityServiceObject bs : data.getObjects()) {
				TobikeStation s = (TobikeStation) bs;

				AlertParking p = new AlertParking();

				StopId t = new StopId();
				t.setAgencyId((String) data.getInfo().get(AGENCY_ID));
				t.setId(s.getNome() + " - " + data.getInfo().get(PLACE));
				p.setPlace(t);

				p.setCreatorId("");
				p.setCreatorType(CreatorType.SERVICE);
				p.setDescription("");
				p.setType(AlertType.PARKING);
				p.setId(s.getNome() + " - " + data.getInfo().get(PLACE));
				p.setNote(s.getIndirizzo());
				p.setPlacesAvailable(s.getPosti());
				p.setNoOfvehicles(s.getBiciclette());

				p.setFrom(System.currentTimeMillis());
				p.setTo(System.currentTimeMillis() + 1000 * 60 * 5);

				p.setId(p.getPlace().getId() + "_" + CreatorType.SERVICE + "_" + p.getFrom() + "_" + p.getTo());

				alertList.add(p);
				
				
				Station station = new Station();
				station.setId(s.getNome() + " - " + data.getInfo().get(PLACE));
				station.setAddress(cleanAddress(s.getIndirizzo()));
				station.setName(s.getNome());
				station.setPosition(new double[]{s.getLatitude(),s.getLongitude()});
				station.setBikes(s.getBiciclette());
				station.setSlots(s.getPosti());
				station.setTotalSlots(getTotal(s.getStato()));
				stations.add(station);				
				
			}
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("parcheggi_" + data.getInfo().get(AGENCY_ID) + ".txt"), alertList);
//			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("stazioni_" + data.getInfo().get(AGENCY_ID) + ".txt"), stations);
			Unirest.post(ConnectionHelper.getInstance().getMobilityURL() + "servicedata/publishBikeStations/" + data.getInfo().get(NAME) + "/" + data.getInfo().get(AGENCY_ID)).header("Accept", "application/json").header("Content-Type", "application/json").basicAuth(ConnectionHelper.getInstance().getUser(), ConnectionHelper.getInstance().getPassword()).body(stations).asString().getStatus();
			return Unirest.post(ConnectionHelper.getInstance().getMobilityURL() + "servicedata/publishAlertParkings").header("Accept", "application/json").header("Content-Type", "application/json").basicAuth(ConnectionHelper.getInstance().getUser(), ConnectionHelper.getInstance().getPassword()).body(alertList).asString().getStatus();
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}

	}
	
	private String cleanAddress(String indirizzo) {
		return indirizzo.replace("\\", "");
	}	
	
	private int getTotal(String stato) {
		if (StringUtils.hasLength(stato)) {
			int result = 0;
			for (int i = 0; i < stato.length(); i++) {
				if (stato.charAt(i)!='x') result++;
			}
			return result;
		}
		return 0;
	}	

}
