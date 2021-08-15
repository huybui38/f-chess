if (!String.prototype.format) {
    String.prototype.format = function() {
      var args = arguments;
      return this.replace(/{(\d+)}/g, function(match, number) { 
        return typeof args[number] != 'undefined'
          ? args[number]
          : match
        ;
      });
    };
  }
function onChange(oldPos, newPos) {
  console.log("Position changed:");
  console.log("Old position: " + Xiangqiboard.objToFen(oldPos));
  console.log("New position: " + Xiangqiboard.objToFen(newPos));
  console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
}

function onDragStart(source, piece, position, orientation) {
  console.log("Drag started:");
  console.log("Source: " + source);
  console.log("Piece: " + piece);
  console.log("Position: " + Xiangqiboard.objToFen(position));
  console.log("Orientation: " + orientation);
  console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
}
const HASH_MESSAGE = {
    "GAME_ROOM.CHAT.PLAYER_JOINED":"Player {0} has joined the room!"
}
const getHashMessage = (hash) => {
    if (!HASH_MESSAGE[hash]) return hash;
    return HASH_MESSAGE[hash];
}
const config = {
  draggable: true,
  dropOffBoard: "snapback",
  position: "start",
  onChange: onChange,
  onDragStart: onDragStart,
};
$(onReady);
function initEvent(socket){
    socket.on("connect", () => {
        console.log("onConnected");
    });
    socket.on("gameRoom", initGameRoomEvent)
    socket.on("notify", (response) => alert(response.msg));
}

function initGameRoomEvent(response){
    console.log(response);
    switch (response.type) {
        case roomEvent.joinRoom:
        case roomEvent.createRoom:
            $("#currentRoom").html(response.roomID);
            $("#roomDetail").show();
            $("#wait").show();
            $("#unJoinedSection").hide();
            $("#joinedSection").show();
            // output(`<span class="connect-msg">User ${ $("#userID").html()} has joined room (${response.roomID})</span>`);
            break;
        case roomEvent.chat:
            if (response.isSystem){
                let msg = getHashMessage(response.message).format(response.data);
                output('<span class="connect-msg">'+msg+'</span>');
            }else {
                output('<span class="username-msg">' + response.userName + ':</span> ' + response.message);
            }
            break;
        case roomEvent.selectTeam:
            $(`#red > span`).html(response.red);
            $(`#black > span`).html(response.black);
            $(`#red  > span`).show();
            $(`#black  > span`).show();
        case roomEvent.roomInfo:
            $(`#red > span`).html(response.red);
            $(`#black > span`).html(response.black);
            $(`#red  > span`).show();
            $(`#black  > span`).show();
            break;
        default:
            break;
    }
}
const roomEvent = {
    joinRoom:1,
    createRoom:2,
    chat:3,
    selectTeam:4,
    roomInfo:5,
    startGame:6
}
function produceEvent(eventType, data){
    return {
        type:eventType,
        data
    }
}
function output(message) {
    var currentTime = "<span class='time'>" + moment().format('HH:mm:ss.SSS') + "</span>";
    var element = $("<div>" + currentTime + " " + message + "</div>");
    $('#console').prepend(element);
 }
 function onSelectTeam(socket, team){
    socket.emit("gameRoom", produceEvent(roomEvent.selectTeam, team));
 }
 function sendStartGame(socket){
    socket.emit("gameRoom", produceEvent(roomEvent.startGame));
 }
let currentRoom = null
function onReady() {
  let userID = localStorage.getItem("token");
  if (!userID) {
    userID = "user" + Math.floor(Math.random() * 1000 + 1);
    localStorage.setItem("token", userID);
  };
  $("#userID").html(userID);
  $("#unJoinedSection").show();
  $("#joinedSection").hide();
  $("#wait").hide();
  $("#roomDetail").hide();
  const socket = io(`ws://localhost:9092/chess?token=${userID}`);
  initEvent(socket);
  $("#btnJoinRoom").on("click", () => {
    let roomID = $("#roomID").val();
    socket.emit("gameRoom", produceEvent(roomEvent.joinRoom, roomID));
  });
  $("#btnCreate").on("click", () => {
    socket.emit("gameRoom", produceEvent(roomEvent.createRoom));
  });
  $("#myBoard").hide();
  $("#red").on("click", () => onSelectTeam(socket, 0));
  $("#black").on("click", () => onSelectTeam(socket, 1));
  $("#btnStartGame").on("click",() => sendStartGame(socket))
  const board = Xiangqiboard("myBoard", config);
}