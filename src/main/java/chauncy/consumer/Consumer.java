package chauncy.consumer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @classDesc: 功能描述(使用点对点通信消费者模式)  
 * @author: ChauncyWang
 * @createTime: 2019年7月12日 下午2:57:44   
 * @version: 1.0
 */
public class Consumer {
	private static final String USERNAME="admin";
	private static final String PASSWORD="admin";
	private static final String BROKERURL="tcp://127.0.0.1:61616";
	private static final String QUEUENAME="myQueue";
	
	public static void main(String[] args) throws JMSException {
		receiver();
	}
	
	static public void receiver() throws JMSException{
		//ConnectionFactory ：连接工厂，JMS 用它创建连接
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKERURL);
		//创建JMS客户端到JMS Consumer 的连接
		Connection connection = activeMQConnectionFactory.createConnection();
		//启动连接
		connection.start();
		//Session： 一个发送或接收消息的线程 |第一个参数：是否要事务；第二个参数，jms设置消息的可靠性，示例为：自动签收
		Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
		//设置队列名称，创建队列，队列名称就是入口。
		Queue queue = session.createQueue(QUEUENAME);
		//创建一个消费者，消息接收者
		MessageConsumer consumer = session.createConsumer(queue);
		while (true) {
			TextMessage message = (TextMessage) consumer.receive();
			if (message != null) {
				System.out.println("收到消息：" + message.getText());
				//session.commit();
				//手动签收方式需要手动应答
				message.acknowledge();
			} else
				break;
		}
		session.close();
		connection.close();
	}
}
