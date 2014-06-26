import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSubscribeSample {

    public static void main(String[] args) {

        String topic        = "MQTT Examples";
        int qos             = 2;
        String broker       = "tcp://m10.cloudmqtt.com:14353";
        String clientId     = "JavaSubSample";
        String userName     = "user2";
        String password     = "user2";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(userName);
            connOpts.setPassword(password.toCharArray());
            sampleClient.setCallback(new MqttCallbackSample());
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Subscribing topic : "+topic);
            sampleClient.subscribe(topic);
            System.out.println("topic subscribed");

            try {
                Thread.currentThread().sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            sampleClient.unsubscribe(topic);
            System.out.println("Unsubscribed");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

    private static class MqttCallbackSample implements MqttCallback {
        public void connectionLost(Throwable cause) {
            cause.printStackTrace();
        }

        public void deliveryComplete(IMqttDeliveryToken token) {
            System.out.println("Token has been delivered: "+token.toString());
        }

        public void messageArrived(String topic, MqttMessage message) {
            System.out.println("Message arrived: "+message+", topic: "+message);
        }
    }
}
