package chauncy.producter;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**   
 * @classDesc: 功能描述(使用点对点通信生产者模式)  
 * @author: ChauncyWang
 * @createTime: 2019年7月9日 下午11:18:17   
 * @version: 1.0  
 */  
public class Producter {
	
	private static final String USERNAME="admin";
	private static final String PASSWORD="admin";
	private static final String BROKERURL="tcp://127.0.0.1:61616";
	private static final String QUEUENAME="myQueue";
	
	public static void main(String[] args) throws JMSException {
		start();
	}
	
	static	public void start() throws JMSException{
		//ConnectionFactory ：连接工厂，JMS 用它创建连接
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKERURL);
		//创建JMS客户端到JMS Provider 的连接
		Connection connection = activeMQConnectionFactory.createConnection();
		//启动连接
		connection.start();
		//Session： 一个发送或接收消息的线程 |第一个参数：是否要事务，如果为true，每生产一个消息都需要进行commit一下；第二个参数，jms设置消息的可靠性，示例为：自动签收
		Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		//设置队列名称，创建队列，队列名称就是入口。
		Queue queue = session.createQueue(QUEUENAME);
		//创建一个生产者
		MessageProducer producer = session.createProducer(queue);
		//设置不持久化
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		//发送一条消息
		for (int i = 1; i <= 5; i++) {
			sendMsg(session, producer, i);
		}
		//设置存放消息队列内容
		//TextMessage textMessage = session.createTextMessage("hello world");
		//producer.send(textMessage);
		session.close();
		//关闭连接
		connection.close();
		System.out.println("消息队列存放内容成功！");
	}
	
	/**
	 * 在指定的会话上，通过指定的消息生产者发出一条消息
	 * 
	 * @param session
	 *            消息会话
	 * @param producer
	 *            消息生产者
	 */
	public static void sendMsg(Session session, MessageProducer producer, int i) throws JMSException {
		// 创建一条文本消息
		TextMessage message = session.createTextMessage("Hello ActiveMQ！" + i);
		// 通过消息生产者发出消息
		producer.send(message);
		//session.commit();
	}

}
