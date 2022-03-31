package com.example.demo.service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Channel;
import net.bis5.mattermost.model.ChannelList;
import net.bis5.mattermost.model.ChannelType;
import net.bis5.mattermost.model.Post;
import net.bis5.mattermost.model.Team;
import net.bis5.mattermost.model.TeamList;
import net.bis5.mattermost.model.User;

@Service
public class DemoService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MattermostClient mattermostClient;
	private ConcurrentHashMap<String, Session> userMap;
	private static final String EMAIL1 = "AAA@example.com";
	private static final String EMAIL2 = "BBB@example.com";
	private static final String EMAIL3 = "CCC@example.com";
	private static final String PASS = "Mattermost1!";
	private static final String TEAM1 = "EEE";
	
	@Data
	@NoArgsConstructor
	public class Session{
		private User user;
		private String email;
		private String token;
		public Session(String email, String token, User user) {
			this.email = email;
			this.token = token;
			this.user = user;
		}
	}
	
	@Autowired
	public DemoService(){
		mattermostClient = MattermostClient.builder()
			    .url("http://localhost:4000")
				.logLevel(Level.INFO)
				.ignoreUnknownProperties()
				.build();
		userMap = new ConcurrentHashMap<>();
	}
	
	private User getUser(final String email) {
		return userMap.get(email).getUser();
	}
	
	private void accessUser(final String email) {
		if(!userMap.containsKey(email)) {
			ApiResponse<User> result = mattermostClient.login(email, PASS);
			String token = result.getRawResponse().getHeaderString("token");
			User user = result.readEntity();
			userMap.put(email, new Session(email, token, user));
			changeUser(email);
		}
	}
	
	private Session changeUser(final String email) {
		Session session = userMap.get(email);
		mattermostClient.setAccessToken(session.getToken());
		mattermostClient.getMe();
		logger.info("login: "+email);
		return session;
	}
	
	public void sendTeamMessage() {
		
	}
	
	public void getChannelListInTeam() {
		login();
		Session session = changeUser(EMAIL1);
		ApiResponse<Team> teamResult = mattermostClient.getTeamByName(TEAM1);
		Team team = teamResult.readEntity();
		ApiResponse<ChannelList> channelResult = mattermostClient.getChannelsForTeamForUser(team.getId(), getUser(EMAIL1).getId());
		ChannelList channelList = channelResult.readEntity();
		for(Channel channel : channelList) {
			logger.info(channel.getDisplayName() + ": " + channel.getType() + " -> ");
		}
//		Channel channel = new Channel("TEST", "test", ChannelType.Open, team.getId());
//		mattermostClient.createChannel(channel);
	}
	
	public void sendGroupMessage() {
		login();
		changeUser(EMAIL1);
		ApiResponse<Channel> res = mattermostClient.createGroupChannel(getUser(EMAIL1).getId(), getUser(EMAIL2).getId(), getUser(EMAIL3).getId());
		Channel channel = res.readEntity();
		mattermostClient.createPost(new Post(channel.getId(), "group Message"));
	}
	
	public void sendDirectMessage() {
		login();
		logger.info("create direct channel");
		changeUser(EMAIL1);
		ApiResponse<Channel> c1 = mattermostClient.createDirectChannel(getUser(EMAIL1).getId(), getUser(EMAIL2).getId());
		Channel channel = c1.readEntity();

		logger.info("send message user1 to user2");
		changeUser(EMAIL1);
		mattermostClient.createPost(new Post(channel.getId(), "test Messsage1"));
		
		logger.info("send message user2 to user1");
		changeUser(EMAIL2);
		mattermostClient.createPost(new Post(channel.getId(), "test Messsage2"));
	}
	
	public void login() {
		accessUser(EMAIL1);
		accessUser(EMAIL2);
		accessUser(EMAIL3);
	}
}
