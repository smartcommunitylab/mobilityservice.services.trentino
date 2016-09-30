/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
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
package it.smartcommunitylab.mobilityservice.services.service.ordinanzerovereto;

import it.smartcommunitylab.mobilityservice.services.MobilityService;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceException;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;
import it.smartcommunitylab.mobilityservice.services.service.ordinanzerovereto.model.Ordinanza;
import it.smartcommunitylab.mobilityservice.services.service.ordinanzerovereto.model.Via;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.Lists;

public class OrdinanzeRoveretoService extends MobilityService {

	public static void main(String[] args) throws MobilityServiceException {
		OrdinanzeRoveretoService s = new OrdinanzeRoveretoService();
		List<MobilityServiceObject> result = s.invokeService();
		System.out.println(result);
	}
	
	@Override
	public List<MobilityServiceObject> invokeService() throws MobilityServiceException {
		
		List<MobilityServiceObject> result = Lists.newArrayList();

		try {
		ObjectMapper mapper = new ObjectMapper();
		List<Map> list = mapper.readValue(new URL("http://www2.comune.rovereto.tn.it/iride/extra/ordinanze_gps/json/"), List.class);
		

//			List<Map> list = new ObjectMapper().readValue(data, List.class);
			for (Map<String,Object> ord : list) {

				Ordinanza ordinanza = new Ordinanza();
				ordinanza.setId(ord.get("DIR_DOCIDE").toString());
				ordinanza.setAl(convertDate(ord.get("PERIODO_AL").toString()));
				ordinanza.setDal(convertDate(ord.get("PERIODO_DAL").toString()));
				ordinanza.setData(convertDate(ord.get("REG_DATA").toString()));
				ordinanza.setOggetto(ord.get("DOC_OGG").toString());
				ordinanza.setStato(ord.get("STATO_ORDINANZA").toString());
				ordinanza.setTipologia(ord.get("TIPO_ORDINANZA").toString());
				List<Map<String,Object>> vie = (List<Map<String, Object>>) ord.get("VIE");
				List<Via> vieList = new ArrayList<Via>();
				if (vie != null) {
					for (Map<String,Object> viaMap : vie) {
						if (viaMap.get("LATITUDINE") == null || viaMap.get("LONGITUDINE").toString().isEmpty()) continue;
						Via via = new Via();
								via.setAlCivico(notNull(viaMap.get("AL_CIVICO")).toString());
								via.setAlIntersezione(notNull(viaMap.get("ALL_INTERSEZIONE")).toString());
								via.setCodiceVia(notNull(viaMap.get("COD_VIA")).toString());
								via.setDalCivico(notNull(viaMap.get("DAL_CIVICO")).toString());
								via.setDalIntersezione(notNull(viaMap.get("DALL_INTERSEZIONE")).toString());
								via.setNote(notNull(viaMap.get("NOTA_VIA")).toString());
								via.setDescrizioneVia(notNull(viaMap.get("NTV_DESCR")).toString());
								via.setLat(Double.parseDouble((viaMap.get("LATITUDINE")).toString()));
								via.setLng(Double.parseDouble((viaMap.get("LONGITUDINE")).toString()));
								via.setTipologia(notNull(viaMap.get("TIPOLOGIA")).toString());
						vieList.add(via);
					}
					ordinanza.setVie(vieList);
				}
//				ordinanza.setTimestamp(System.currentTimeMillis());
				result.add(ordinanza);
			}
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
				
				
		return result;
	}
	
	private String convertDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		Date newDate = sdf.parse(date);
		Calendar cal = new GregorianCalendar();
		cal.setTime(newDate);
		return cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
	}
	
	private Object notNull(Object s) {
		return s == null ? "" : s;
	}	
	
	

	/**
	 * Convert strange input format to JSON
	 * @param txt
	 * @return
	 */
	private static String preprocess(String txt) {
		txt = txt.replace("\t", "");
		txt = txt.replace("\n", "");
		txt = txt.replace("\r", "");
		// strange array to JSON object
		txt = txt.replace("[\"", "{\"");
		txt = txt.replace("],", "},");
		// strange object to array
		txt = txt.replace("\"vie\": {", "\"vie\": [");
		txt = txt.replace("}}}", "]}}");
		while (txt.indexOf("\"dal\"") >= 0) {
			txt = txt.replaceFirst("\"dal\"", "\"from\"");
			txt = txt.replaceFirst("\"dal\"", "\"to\"");
			txt = txt.replaceFirst("\"al\"", "\"to\"");
		}
		
		
		char[] arr = txt.toCharArray();
		StringBuilder sb = new StringBuilder();
		boolean opened = false;
		for (int i = 0; i < arr.length; i++) {
			if (opened && arr[i] == '"' && arr[i+1] != ',' && arr[i+1] != ':') {
				sb.append("\\\"");
			} else if (opened || arr[i]!=' ') {
				sb.append(arr[i]);
			}
			
			if (arr[i] == '"' && (arr[i+1] == ',' || arr[i+1] == ':')) {
				opened = false;
			} else if (arr[i] == '"' && ! opened) {
				opened = true;
			}

			if ((arr[i]=='}' || arr[i]==']') && sb.charAt(sb.length()-2) == ',') {
				sb.deleteCharAt(sb.length()-2);
			}
		}
		txt = sb.toString();
		return txt;
	}

}
