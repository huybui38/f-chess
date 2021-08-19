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
  console.log("Position: " + Xiangqiboard.objToFen(position) +  "--" + JSON.stringify(position));
  console.log("Orientation: " + orientation);
  console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
}
function onDrop(source, target, piece, newPos, oldPos, orientation){
    window.socket.emit("gameData", produceEvent(gameEvent.gameData,{
        source,
        target
    }));
    return 'snapback';
}
const HASH_MESSAGE = {
    "GAME_ROOM.CHAT.PLAYER_JOINED":"Player {0} has joined the room!"
}
const TEAM = {
    RED:1,
    BLACK:0
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
  showNotation: true,
  onDragStart: onDragStart,
  onDrop:onDrop
};
$(onReady);
function initEvent(socket){
    socket.on("connect", () => {
        console.log("onConnected");
    });
    socket.on("gameRoom", initGameRoomEvent)
    socket.on("gameData", initGameDataEvent)
    socket.on("notify", (response) => alert(response.msg));
}
function toggleTurn(turn){
    if (turn === TEAM.RED){
        $("#red").toggleClass("current", true);
        $("#black").toggleClass("current", false);
    }else {
        $("#red").toggleClass("current", false);
        $("#black").toggleClass("current", true);
    }
}
function initGameDataEvent(response){
    console.log(response);
    switch (response.type) {
        case gameEvent.gameData:
            let pos = response.position;
            let turn = response.turn;
            console.log(`pos:${pos} - turn: ${turn}`);
            toggleTurn(turn);
            window.board.position(pos, true);
            break;
        case gameEvent.gameSurrender:
            break;
        case gameEvent.gameEnd:
            alert("Winner: "+ response.winnerName);
            break;
    }
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
                output('<span class="username-msg">' + response.data + ':</span> ' + response.message);
            }
            break;
        case roomEvent.startGame:
            console.log("game start...")
            toggleTurn(TEAM.RED);
            $("#myBoard").show();
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
            $("#hostPlayer").html(response.host);
            if (response.isPlaying){
                $("#myBoard").show();
                toggleTurn(response.turn);
                window.board.position(response.currentPosition, true);
                if (!response.isViewer){
                    window.board.orientation(getTeamName(response.team));
                }
            }
            break;
        case roomEvent.exitRoom:
            output('<span class="connect-msg">'+response.name+' exited</span>');
            break;
        default:
            break;
    }
}
function getTeamName(team){
    return team === 1 ? 'red' : 'black'
}
const roomEvent = {
    joinRoom:1,
    createRoom:2,
    chat:3,
    selectTeam:4,
    roomInfo:5,
    startGame:6,
    resetRoom:7,
    exitRoom:8
}
const gameEvent = {
    gameData:0,
    gameSurrender:3,
    gameEnd:4
}
function produceEvent(eventType, data){
    return {
        type:eventType,
        data
    }
}
function reset(){
    $("#userID").html(userID);
    $("#unJoinedSection").show();
    $("#joinedSection").hide();
    $("#wait").hide();
    $("#roomDetail").hide();
    $("#myBoard").hide();
}
function output(message) {
    var currentTime = "<span class='time'>" + moment().format('HH:mm:ss.SSS') + "</span>";
    var element = $("<div>" + currentTime + " " + message + "</div>");
    $('#console').prepend(element);
 }
 function onSelectTeam(socket, team){
    socket.emit("gameRoom", produceEvent(roomEvent.selectTeam, team));
    window.board.orientation(getTeamName(team));
 }
 function sendStartGame(socket){
    let time = $("#time").val();
    socket.emit("gameRoom", produceEvent(roomEvent.startGame, time));
 }
 function sendSurrenderGame(socket){
    socket.emit("gameData", produceEvent(gameEvent.gameSurrender));
 }
 function sendExitRoom(socket){
    socket.emit("gameRoom", produceEvent(roomEvent.exitRoom));
    reset();
 } 
 function sendChat(socket){
    let msg = $("#chat").val();
    socket.emit("gameRoom", produceEvent(roomEvent.chat, msg));
 }
let currentRoom = null;
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
  $("#red").on("click", () => onSelectTeam(socket, 1));
  $("#black").on("click", () => onSelectTeam(socket, 0));
  $("#btnStartGame").on("click",() => sendStartGame(socket))
  $("#btnSurrender").on("click",() => sendSurrenderGame(socket))
  $("#btnExitRoom").on("click",() => sendExitRoom(socket))
  $("#btnChat").on("click",() => sendChat(socket))
  window.board = Xiangqiboard("myBoard", config);
  window.socket = socket;
}