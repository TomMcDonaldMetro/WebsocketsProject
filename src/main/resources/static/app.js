/**
	Document Notes:
	
	1, Connect to the server. on-connect -> subscribe to the personal replies channel.
	2, Personal replies channel on being subscribed to should reply with a list of all of the channels.
	3, Channels are then built via JavaScript accessing our shadowDOM template.
	4, If a user clicks on join -> two things happen:
		1, the user is subscribed to /queue/game/{room} and /queue/{room} and the chat appears.
		2, the channel templates are removed from the DOM. (single-page application).
	5, If a user wishes to leave their current channel, a button (which appeared on entry) will unsubscribe from {room}
		and the replies channel will once again be invoked to generate a list of channels from the server.
		to once more be written to the screen by JavaScript. repeating at (3).
	
	User Requirements:
	
	1, When a room is full (2/2) disable the html button.
	2, The server maintains a list of {rooms} and their occupants.
		a subscribe and a disconnect message updates accordingly.
	3, There will be no (0/2) rooms. If the room is empty, it will be deleted from the server.
	4, The rooms operate on a first-come first-serve basis. A disconnect or a thread-race decide occupants.
	5, Reset button hit by either player will reset the current game.
	6, The current game will persist through Disconnects as long as one player remains inside the channel at all times.

 */


const stompClient = new StompJs.Client({
	brokerURL: 'ws://192.168.2.27:8080/chat'
});


stompClient.onWebSocketError = (error) => {
	console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
	console.error('Broker reported error: ' + frame.headers['message']);
	console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	}
	else {
		$("#conversation").hide();
	}
	$("#greetings").html("");
}

/** Deprecated for the time being
function connect() {
	stompClient.activate();
}
 */

/**
	Testing channel for building functionality for dynamically displaying channels with players in them.
 */
function connect() {
	stompClient.activate();

	console.log("connect room");
	stompClient.onConnect = (frame) => {
		setConnected(true);
		console.log('Connected: ' + frame);
		connectMessage();
		const sub = stompClient.subscribe('/user/queue/reply', (reply) => {

			let connections = JSON.parse(reply.body).channelMap;

			console.log("subscribed to /user/queue/reply");
			const map = Object.entries(connections).map(([k, v]) => {
				return [k, v.length];
			})
			console.log(map);
			displayRooms(map);

		});
		console.log(sub);
		console.log("connect room");
	};
}

/**
	Take a 2d array of [channel, numberOccupants]
	create a channeltemplate and append to the channels div
	
	TODO: addEventListener: clicking is supposed to subscribe us to the room's queue and game channels.
 */
function displayRooms(rooms) {
	const div = document.getElementById("channels");
	const template = document.getElementById("channeltemplate");
	rooms.forEach(room => {


		const clone = template.content.cloneNode(true);
		let p = clone.querySelectorAll("p");
		p[0].innerHTML = `${room[0]}'s room ${room[1]}/2`;
		let b = clone.querySelectorAll("button");
		b[0].innerHTML = `Join`;
		b[0].addEventListener("click", ()=>{console.log("clicked the button we need to subscribe etc...")})
		
		div.appendChild(clone);
	});

}

function connectMessage() {
	console.log("connect message");
	stompClient.publish({
		destination: `/app/connected`,
		body: JSON.stringify({ 'name': $("#name").val(), 'message': "has joined." })
	});
}

let roomName = "";
let begin = false;
function connectRoom() {
	stompClient.activate();
	roomName = $("#privateroom").val();
	let button = "#button";
	console.log("connected to room: ", roomName);
	stompClient.onConnect = (frame) => {
		setConnected(true);
		console.log('Connected: ' + frame);
		joinMessage();
		const subChannel = stompClient.subscribe(`/queue/${roomName}`, (reply) => {
			console.log(reply.body)
			let b = JSON.parse(reply.body).content;

			if (b == "Game Over!") {
				$(".tile").prop("disabled", true);
			}

			if (b == "rgtbegin" && !begin) {
				begin = true;
				createBoard();
			}
			showGreeting(JSON.parse(reply.body).content);
		}, $("#name").val());

		const subGame = stompClient.subscribe(`/queue/game/${roomName}`, (reply) => {
			console.log(reply.body)
			let board = JSON.parse(reply.body).board;
			let val = JSON.parse(reply.body).val;
			let move = JSON.parse(reply.body).move;
			console.log(typeof (board));
			console.log(board);
			let message = JSON.parse(reply.body).message;

			for (let i = 0; i < 9; i++) {
				$(`#button${i}`).html(board[i]);
			}

			showGreeting(`${message}`);
		}, $("#name").val());


		const personal = stompClient.subscribe(`/user/queue/reply`, (reply) => {
			console.log("Reply body", reply)
			let connections = JSON.parse(reply.body).channelMap;
			console.log(connections);
			/*
			Object.keys(connections).forEach((key) => {
				console.log(`${key}`);
			});
			Object.values(connections).forEach((value) => {
				console.log(`${value}`)
			})
			*/

			const map = Object.entries(connections).map(([k, v]) => {
				return `<div> <p>${k} ${v.length}/2</p> </div>`
			})
			console.log(map);

			Object.keys(connections).forEach(key => {
				let value = connections[key];
				console.log(`${key} = ${value}`);
			})
			//console.log("connections",connections);
			//showGreeting(`${JSON.parse(reply.body).prev} has played:  ${val}`);
		}, $("#name").val());

	};


}


/*
*	Registered to the games buttons only.
*	Click the button and it will send the associated number to the 
*	Game Move Controller.
*/
function clicked(i) {
	sendMove(i);
}

/*
	Only thing to communicate with the game move controller.
*/
function sendMove(i) {
	stompClient.publish({
		destination: `/app/game/move/${roomName}`,
		body: JSON.stringify({ 'num': `${i}`, 'prev': $("#name").val() })
	})
}

function sendMessage() {
	console.log("send message");
	stompClient.publish({
		destination: `/app/game/${roomName}`,
		body: JSON.stringify({ 'name': $("#name").val(), 'message': $("#message").val() })
	});
	clear();
}

function joinMessage() {
	console.log("join message");
	stompClient.publish({
		destination: `/app/${roomName}`,
		body: JSON.stringify({ 'name': $("#name").val(), 'message': "has joined." })
	});
}



function disconnect() {
	stompClient.deactivate();
	setConnected(false);
	console.log("Disconnected");
}

function clear() {
	$("#message").val("");
}

function sendName() {
	stompClient.publish({
		destination: "/app/hello",
		body: JSON.stringify({ 'name': $("#name").val() })
	});
}

function showGreeting(message) {
	$("#greetings").append("<tr><td>" + message + "</td></tr>");
}


function createBoard() {
	const template = document.querySelector('template');
	const node = document.importNode(template.content, true);
	node.children[0].addEventListener("click", function(event) {
		const isbutton = event.target.nodeName === 'BUTTON';
		if (!isbutton) {
			return;
		}
		console.log("event id" + event.target.id);
		console.log("event value" + event.target.value);
		clicked(event.target.value);
	});
	const container = document.getElementById("gamecontainer");
	container.appendChild(node);

}

$(function() {
	$("form").on('submit', (e) => e.preventDefault());
	$("#connect").click(() => connect());
	$("#private").click(() => connectRoom());
	$("#disconnect").click(() => disconnect());
	$("#send").click(() => sendMessage());
	$(".tile").click(function() {
		var fired_button = $(this).val();
		console.log(fired_button);
		clicked(fired_button);
	});


});