let stompClient = null;

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  } else {
    $("#conversation").hide();
  }
  $("#messages").html("");
}


function connect() {
  const socket = new SockJS('/my-websocket');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, (frame) => {
    setConnected(true);
    console.log('Connected:', frame);
    sendMessageFirst();
    stompClient.subscribe('/topic/messages', (message) => {
      showMessage(JSON.parse(message.body).content);
      sendMessage();
    });
  });
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}


function sendMessageFirst() {
  stompClient.send("/app/message", {}, JSON.stringify({'content': "first"}));
}

function sendMessage() {
  stompClient.send("/app/message", {}, JSON.stringify({'content': $("#message").val()}));
}

function showMessage(message) {
    if(message!=""){
        $("#messages").empty();
        $("#messages1").empty();
        $("#messages2").empty();
        let substrings = message.split("||");
        let subDown = substrings[0].split(",");
        let subBarrel = substrings[1].split(",");
        let subtop10 = substrings[2].split("::");

        for (let i = 0; i < subDown.length; i++) {
          $("#messages").append("<tr><td>" + subDown[i] + "</td></tr>");
        }


        for (let i = 0; i < subBarrel.length; i++) {
          $("#messages1").append("<tr><td>" + subBarrel[i] + "</td></tr>");
        }

        for (let i = 1; i < subtop10.length; i++) {
            $("#messages2").append("<tr><td>" + subtop10[i] + "</td></tr>");
        }

    }
}

$(function () {
  $("form").on('submit', (e) => {
    e.preventDefault();
  });

  $("#send").click(() => {
    sendMessage();
  });
});



window.onload = connect;
window.onbeforeunload = disconnect;

