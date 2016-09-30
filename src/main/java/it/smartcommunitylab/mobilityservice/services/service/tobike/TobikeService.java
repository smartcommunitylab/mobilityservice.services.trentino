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

import it.smartcommunitylab.mobilityservice.services.MobilityService;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceException;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;
import it.smartcommunitylab.mobilityservice.services.service.tobike.model.TobikeStation;
import it.smartcommunitylab.mobilityservice.services.service.tobike.ws.TOBikeUtente;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

@Component
public class TobikeService extends MobilityService {

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
//				tobikeStation.setTimestamp(System.currentTimeMillis());

				result.add(tobikeStation);
			}

			return result;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

}
