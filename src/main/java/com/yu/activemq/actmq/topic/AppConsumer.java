package com.yu.activemq.actmq.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/***
 * 消费者
 * 当有多个消费者时，每个消费者会完整的消费数据
 * 
 * 特别注意：
 * 主题模式与队列模式的区别
 * 主题模式必须是先向订阅者预定消息，才能够接受消息，队列模式是不需要提前预定
 * 也就是说：队列模式下，队列的消费者，在启动的时候队列里面有消息，它都会消费
 * 主题模式下，主题的消费者不能消费在它启动之前的消息
 * 
 * */
public class AppConsumer {
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
			
			//创建会话（第一个参数，是否是创建事物，第二个为自动确认，客户端发送和接收消息不需要做额外的工作）
			Session session  = c.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			// 创建目的地
			Destination dt = session.createTopic(topicName);
			
			//创建消费者
			MessageConsumer messageConsumer = session.createConsumer(dt);
			
			//创建监听器
			messageConsumer.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					try {
						//这里请注意，在producter里面怎么设置，这里就用什么方式获取
						TextMessage tm = (TextMessage)message;
						System.out.println("消费："+tm.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
		}
}
