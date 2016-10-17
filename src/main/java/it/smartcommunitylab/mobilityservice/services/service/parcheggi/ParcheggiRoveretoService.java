package it.smartcommunitylab.mobilityservice.services.service.parcheggi;

import it.sayservice.platform.smartplanner.data.message.StopId;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertParking;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertType;
import it.sayservice.platform.smartplanner.data.message.alerts.CreatorType;
import it.smartcommunitylab.mobilityservice.services.MobilityService;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceException;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObjectsContainer;
import it.smartcommunitylab.mobilityservice.services.service.parcheggi.model.Parcheggio;
import it.smartcommunitylab.mobilityservice.services.util.AuthHelper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;

import com.google.common.collect.Lists;
import com.mashape.unirest.http.Unirest;

@Component
public class ParcheggiRoveretoService extends MobilityService {

	@Override
	protected List<MobilityServiceObject> invokeService() throws MobilityServiceException {
		try {
			List<MobilityServiceObject> result = Lists.newArrayList();

			Object res;
			XmlRpcClient client = new XmlRpcClient("http://88.51.119.253:8070/RPC2", true);
			res = client.invoke("pGuide.getElencoIdentificativiParcheggi", new Object[0]);

			for (Object o : (XmlRpcArray) res) {
				try {
					XmlRpcArray result2;
					Parcheggio parcheggio = new Parcheggio();

					result2 = (XmlRpcArray) client.invoke("pGuide.getCaratteristicheParcheggio", new Object[] { (Integer) o });
					String id = result2.getString(1);
					String ids[] = id.split("-");
					String address = ids[ids.length - 1].trim();
					parcheggio.setId(address);
					parcheggio.setAddress(address);

					result2 = (XmlRpcArray) client.invoke("pGuide.getPostiLiberiParcheggio", new Object[] { (Integer) o });
					parcheggio.setPlaces("" + result2.getInteger(1));

					result.add(parcheggio);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return result;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	@Override
	protected int publishData(MobilityServiceObjectsContainer data) throws MobilityServiceException {
		try {
			List<AlertParking> list = new ArrayList<AlertParking>();
			for (MobilityServiceObject bs : data.getObjects()) {
				Parcheggio p = (Parcheggio) bs;

				AlertParking ap = new AlertParking();

				StopId t = new StopId();
				t.setAgencyId("COMUNE_DI_ROVERETO");
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
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("parcheggi_rovereto.txt"), list);
			return Unirest.post("http://localhost:8080/core.mobility/servicedata/publishAlertParkings").header("Accept", "application/json").header("Content-Type", "application/json").basicAuth(AuthHelper.getInstance().getUser(), AuthHelper.getInstance().getPassword()).body(list).asString().getStatus();

		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}
}
