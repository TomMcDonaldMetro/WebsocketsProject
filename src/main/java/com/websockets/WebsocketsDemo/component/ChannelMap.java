package com.websockets.WebsocketsDemo.component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class ChannelMap {

	public ConcurrentHashMap<String, Set<String>> channelMap;
	
	public ChannelMap() {
		System.out.println("spring created a channel map");
		this.channelMap = new ConcurrentHashMap<String, Set<String>>();
		Set<String> test = new HashSet<String>();
		test.add("test");
		channelMap.put("test", test);
		test.add("testagain");
		Set<String> test2 = new HashSet<String>();
		test2.add("test2");
		channelMap.put("test2", test2);
	}
	 
	public ChannelMap(ConcurrentHashMap<String, Set<String>> map) {
		this.channelMap = map;
	}
	
	// if the channel doesn't exist, create and add the first player.
	// else grab the channel and add the second player.
	// TODO obligatory WARNING THIS IS ASSUMING YOU DON'T MESS UP THE LOGIC.
	// TODO future proof: split up the logic so that a channel is created and we add players to it.
	public void addChannel(String channelName, String username) {
		
		if(channelMap.get(channelName) == null) {
			//System.out.println("inside the add channel");
			channelMap.put(channelName, new HashSet<>());
			channelMap.get(channelName).add(username);
		} else {
			//System.out.println("inside the else channel");

			channelMap.get(channelName).add(username);
		}
		
	}

	public Map<String, Set<String>> getChannelMap() {
		return channelMap;
	}

	public void setChannelMap(ConcurrentHashMap<String, Set<String>> channelMap) {
		this.channelMap = channelMap;
		//System.out.println("Spring called the setter for channel map");
		//System.out.println(this.channelMap.size());
	}
	
	public Set<String> get(String room) {
		return this.channelMap.get(room);
	}
	
	public void remove(String room) {
		this.channelMap.remove(room);
	}
	
}
