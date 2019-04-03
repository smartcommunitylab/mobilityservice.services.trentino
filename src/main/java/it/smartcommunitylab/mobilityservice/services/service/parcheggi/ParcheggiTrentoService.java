package it.smartcommunitylab.mobilityservice.services.service.parcheggi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mashape.unirest.http.Unirest;

import it.sayservice.platform.smartplanner.data.message.StopId;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertParking;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertType;
import it.sayservice.platform.smartplanner.data.message.alerts.CreatorType;
import it.smartcommunitylab.mobilityservice.services.MobilityService;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceException;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObjectsContainer;
import it.smartcommunitylab.mobilityservice.services.service.parcheggi.model.Parcheggio;
import it.smartcommunitylab.mobilityservice.services.util.ConnectionHelper;

public class ParcheggiTrentoService extends MobilityService {

	private	ObjectMapper mapper = new ObjectMapper();
	
	@Override
	protected List<MobilityServiceObject> invokeService() throws MobilityServiceException {
		List<MobilityServiceObject> result = Lists.newArrayList();
		
		try {
		String res = Unirest.get(ConnectionHelper.getInstance().getTrentoParkingUrl()).asString().getBody();
		List<Map> parkings = mapper.readValue(res, new TypeReference<List<Map>>() {
		});
		
		for (Map parking: parkings) {
			Parcheggio pb = new Parcheggio();
			pb.setId((String)parking.get("id"));
			pb.setAddress("");
			pb.setPlaces(((Integer)parking.get("free")).toString());
			
			result.add(pb);
		}
		
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
		
		return result;
	}

	@Override
	protected int publishData(MobilityServiceObjectsContainer data) throws MobilityServiceException {
		try {
			List<AlertParking> list = new ArrayList<AlertParking>();
			for (MobilityServiceObject bs : data.getObjects()) {
				Parcheggio p = (Parcheggio) bs;
				
				AlertParking ap = new AlertParking();

				StopId t = new StopId();
				t.setAgencyId("COMUNE_DI_TRENTO");
				t.setId(p.getId());
				ap.setPlace(t);

				ap.setCreatorId("");
				ap.setCreatorType(CreatorType.SERVICE);
				ap.setDescription("");
				ap.setType(AlertType.PARKING);
				ap.setId(p.getId());
				ap.setNote(p.getAddress());
				ap.setPlacesAvailable(Integer.parseInt(p.getPlaces()));
				ap.setNoOfvehicles(-1);

				ap.setFrom(System.currentTimeMillis());
				ap.setTo(System.currentTimeMillis() + 1000 * 60 * 5);

				ap.setId(ap.getPlace().getId() + "_" + CreatorType.SERVICE + "_" + ap.getFrom() + "_" + ap.getTo());

				list.add(ap);				
				
			}
			
			return Unirest.post(ConnectionHelper.getInstance().getMobilityURL() + "servicedata/publishAlertParkings").header("Accept", "application/json").header("Content-Type", "application/json").basicAuth(ConnectionHelper.getInstance().getUser(), ConnectionHelper.getInstance().getPassword()).body(list).asString().getStatus();
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

}
