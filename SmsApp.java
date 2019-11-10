import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;


import static spark.Spark.*;

    public class SmsApp {
        private static String sentence = null;



        public static void main(String[] args){


            get("/", (req, res) -> "Success");


            post("/sms", (req, res) -> {


                res.type("application/xml");
                //Request gives one long string, so must split to get the body of the text
                String test = req.body();
                //Spaces were represented as "+"
                String[] test2 = test.replace("+", " ").split("\\&");
                //Body was the 10th index and the content starts at the 5th character
                String text = test2[10].substring(5);
                System.out.println(test);
                System.out.println(text);


                //Can ask for Hot 100, Country, Hip-Hop, or R&B
                switch(text)
                {
                    case "Hot 100":

                        try {
                            Document doc = Jsoup.connect("https://www.billboard.com/charts/hot-100")
                                    .get();
                            System.out.println("Title: " + doc.title());
                            Element repository = doc.getElementsByClass("chart-element__information__song").first();
                            String title = repository.text();
                            String artist = doc.getElementsByClass("chart-element__information__artist").first().text();
                            String duration = doc.getElementsByClass("chart-element__information__delta__text text--week").first().text();
                            sentence = title + " by " + artist + " is the #1 song of the week and has been #1 for " + duration;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "Country":
                        try {
                            Document doc = Jsoup.connect("https://www.billboard.com/charts/country-songs")
                                    .get();
                            System.out.println("Title: " + doc.title());
                            Element repository = doc.getElementsByClass("chart-list-item__title").first();
                            String title = repository.text();
                            String artist = doc.getElementsByClass("chart-list-item__artist").first().text();
                            String duration = doc.getElementsByClass("chart-number-one__weeks-at-one").first().text();
                            sentence = title + " by " + artist + " is the #1 song of the week and has been #1 for " +
                                    duration + "weeks";

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "Hip-Hop":

                    case "R and B":

                        try {
                            Document doc = Jsoup.connect("https://www.billboard.com/charts/r-b-hip-hop-songs")
                                    .get();
                            System.out.println("Title: " + doc.title());
                            Element repository = doc.getElementsByClass("chart-list-item__title-text").first();
                            String title = repository.text();
                            String artist = doc.getElementsByClass("chart-list-item__artist").first().text();
                            String duration = doc.getElementsByClass("chart-list-item__weeks-on-chart").first().text();
                            sentence = title + " by " + artist + " is the #1 song of the week and has been on the charts for "
                                    + duration + " weeks";

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        sentence="Please choose one of the following: \n" + "Hot 100\n" + "Country\n" + "Hip-Hop\n" + "R and B";
                        break;

                }

                //Send text to user
                Body body = new Body
                        .Builder(sentence)
                        .build();
                Message sms = new Message
                        .Builder()
                        .body(body)
                        .build();
                MessagingResponse twiml = new MessagingResponse
                        .Builder()
                        .message(sms)
                        .build();
                return twiml.toXml();
            });

        }

    }



