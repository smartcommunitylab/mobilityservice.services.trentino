package it.smartcommunitylab.mobilityservice.services.service.oraritreni;

import it.sayservice.xss.api.XSSParserConfiguration;
import it.sayservice.xss.api.data.XSSData;
import it.sayservice.xss.api.data.XSSEvent;
import it.sayservice.xss.api.handler.ContentHandler;
import it.sayservice.xss.api.handler.EventHandler;
import it.sayservice.xss.api.parser.XSSParserFactory;
import it.sayservice.xss.internal.data.XSSDataImpl;
import it.sayservice.xss.internal.dom.xml.XMLNode;
import it.smartcommunitylab.mobilityservice.services.MobilityService;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceException;
import it.smartcommunitylab.mobilityservice.services.MobilityServiceObject;
import it.smartcommunitylab.mobilityservice.services.service.oraritreni.model.PartenzeArrivi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.codehaus.jackson.map.ObjectMapper;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.parser.HtmlParser;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;

@Component
public class TreniService extends MobilityService {

	private XSSParserFactory factory = null;
	private XSSParserConfiguration config = null;

	public TreniService() {
		factory = XSSParserFactory.newInstance();
		config = new XSSParserConfiguration();
		config.set(XSSParserConfiguration.CONTENT_HANDLER, new ContentHandler() {

			public boolean compare(String key, Serializable value) {
				return true;
			}
		});

		config.set(XSSParserConfiguration.EVENT_HANDLER, new EventHandler() {

			public void onMessage(XSSEvent event) {
				System.err.println("onMessage: " + event.getDescription());
			}

		});
		config.set(XSSParserConfiguration.NODE_NAME_IGNORE_CASE, true);
	}

	@Override
	public List<MobilityServiceObject> invokeService() throws MobilityServiceException {
		try {
			if (parameters == null || parameters.keySet().size() != 1) {
				throw new MobilityServiceException("Needed 1 parameter.");
			}			
			
			ObjectMapper mapper = new ObjectMapper();
			
			List<MobilityServiceObject> result = Lists.newArrayList();

			URL url = new URL("http://mobile.viaggiatreno.it/vt_pax_internet/mobile/stazione");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);

			OutputStream out = conn.getOutputStream();
			Writer writer = new OutputStreamWriter(out, "UTF-8");
			writer.write("stazione=" + (String)parameters.get("stazione") + "&" + "lang=IT");
			writer.close();
			out.close();

			if (conn.getResponseCode() < 200 || conn.getResponseCode() > 299) {
				throw new MobilityServiceException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			factory.deploy("treni", new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("treni.xss")));
			Node xmlNode = readNode(conn.getInputStream());
			XSSData data = factory.newParser("treni").parse(new XMLNode(xmlNode), config);

			String json = XSSDataToJSON(data, false, false);
//			System.out.println(json);
			PartenzeArrivi pa = mapper.readValue(json, PartenzeArrivi.class);

			result.add(pa);
			
			return result;
		} catch (Exception e) {
			throw new MobilityServiceException(e);
		}
	}

	private Element readNode(InputStream is) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			Document document = docBuilder.newDocument();

			UserAgentContext uacontext = new SimpleUserAgentContext();
			HtmlParser parser = new HtmlParser(uacontext, document);

			parser.parse(new InputStreamReader(is));

			return document.getDocumentElement();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String XSSDataToJSON(XSSData data, boolean first, boolean last) {
		String s = "";
		if (first) {
			s = "[\n{\n";
		} else {
			s = "{\n";
		}
		for (String key : data.getNames()) {
			List<Object> val = data.get(key);
			String vs = "";
			for (int i = 0; i < val.size(); i++) {
				Object o = val.get(i);
				if (o instanceof XSSDataImpl) {
					XSSDataImpl impl = (XSSDataImpl) o;
					vs += XSSDataToJSON(impl, val.size() > 1 && i == 0, val.size() > 1 && i == val.size() - 1);
				} else {
					vs += o.toString();
				}
				if (i < val.size() - 1)
					vs += ", ";
			}

			s += "\"" + key + "\" : ";
			if (!vs.startsWith("{") && !vs.startsWith("[")) {
				s += "\"";
			}
			s += vs;
			if (!vs.startsWith("{") && !vs.startsWith("[")) {
				s += "\"";
			}
			s += ",\n";
		}
		s = s.substring(0, s.length() - 2);
		if (last) {
			s += "\n}\n]\n";
		} else {
			s += "\n}\n";
		}
		return s;
	}

}
