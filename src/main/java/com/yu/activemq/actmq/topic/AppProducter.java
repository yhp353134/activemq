package com.yu.activemq.actmq.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

//生产者（主题模式） 
public class AppProducter {

	//61616是activemq 默然的端口
	private static final String url="tcp://192.168.249.88:61616";
	private static final String topicName ="queue-topic";
	
	public static void main(String[] args) throws JMSException {
		//创建连接工程
		ConnectionFactory cf=new ActiveMQConnectionFactory(url);
		
		//创建连接
		Connection c = cf.createConnection();
		
		//启动连接
		c.start();
		
		/***
		 * createSession(paramA,paramB);
			paramA 取值有 : true or false 表示是否支持事务 
			paramB 取值有:Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE，SESSION_TRANSACTED
			createSession(paramA,paramB); 
			paramA是设置事务的，paramB设置acknowledgment mode 
			paramA设置为false时：paramB的值可为Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE其中一个。 
			paramA设置为true时：paramB的值忽略， acknowledgment mode被jms服务器设置为SESSION_TRANSACTED 。 
			Session.AUTO_ACKNOWLEDGE为自动确认，客户端发送和接收消息不需要做额外的工作。 
			Session.CLIENT_ACKNOWLEDGE为客户端确认。客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会删除消息。 
			DUPS_OK_ACKNOWLEDGE允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。在需要考虑资源使用时，这种模式非常有效
		 */
		//创建会话（第一个参数，是否是创建事物，第二个为自动确认，客户端发送和接收消息不需要做额外的工作）
		Session session  = c.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		// 创建目的地
		Destination dt = session.createTopic(topicName);
		
		// 创建一个生产者
		MessageProducer producter = session.createProducer(dt);
		
		for(int i=0;i<100;i++) {
			//创建消息
			// JMS规范中的消息类型包括TextMessage、MapMessage、ObjectMessage、BytesMessage、和StreamMessage等五种
			TextMessage textMessage = session.createTextMessage(); //或者 session.createTextMessage("消息"+i); 
			textMessage.setText("消息"+i);
			
			//map消息
			MapMessage mapMessage = session.createMapMessage();
			mapMessage.setString(i+"", "map消息"+i);
			
			//发布消息
			producter.send(textMessage);
			System.out.println("消息"+i);
		}
		//关闭连接,一定要关闭
		c.close();
	}
	
}
