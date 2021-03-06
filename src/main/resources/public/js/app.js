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
        window.roomsListTimer = setInterval(() => requestListRoom(socket), 2000);
    });
    socket.on("gameRoom", initGameRoomEvent)
    socket.on("gameData", initGameDataEvent)
    socket.on("notify", (response) => alert(response.msg));
    requestListRoom(socket);
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
            onEndGame();
            
            alert("Winner: "+ response.winnerName);
            break;
        case gameEvent.gameSync:
            $(`#red > .time-left`).html(response.time[TEAM.RED]);
            $(`#black > .time-left`).html(response.time[TEAM.BLACK]);
            break;
    }
}
function onEndGame(){
    console.log(`onEndGame`);
    $("#btnStartGame").prop("disabled", false);
    $("#btnSurrender").prop("disabled", true);
    $("#time").prop("disabled", false);
    if (!window.game.isHost){
        disableForNonHostPLayer();
    }
    window.game.isPlaying = false;
}
function initGameRoomEvent(response){
    console.log(response);
    switch (response.type) {
        case roomEvent.joinRoom:
        case roomEvent.createRoom:
            clearInterval(window.roomsListTimer);
            $("#currentRoom").html(response.roomID);
            $("#roomDetail").show();
            $("#wait").show();
            $("#unJoinedSection").hide();
            $("#joinedSection").show();
            $('#console').html("");
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
            onStartGame();
            $("#myBoard").show();
            break;
        case roomEvent.selectTeam:
            $(`#red > .label-player`).html(response.red);
            $(`#black > .label-player`).html(response.black);
            $(`#red  > .label-player`).show();
            $(`#black  > .label-player`).show();
            break;
        case roomEvent.roomInfo:
            $(`#red > .label-player`).html(response.red);
            $(`#black > .label-player`).html(response.black);
            $(`#red  > .label-player`).show();
            $(`#black  > .label-player`).show();
            window.game.isHost = response.isHost;
            if (!response.isHost){
                disableForNonHostPLayer();
            }
            $("#hostPlayer").html(response.host);
            if (response.isPlaying){
               
                onStartGame();
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
        case roomEvent.listRoom:
            renderListRoom(response);
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
    exitRoom:8, 
    listRoom:9
}
const gameEvent = {
    gameData:0,
    gameSurrender:3,
    gameEnd:4,
    gameSync:5
}
function produceEvent(eventType, data){
    return {
        type:eventType,
        data
    }
}
function showJoinedSection(){
    $("#myBoard").show();
}
function hideJoinedSection(){
    $("#myBoard").hide();
    $("#unJoinedSection").show();
    $("#joinedSection").hide();
}
function reset(){
    clearInterval(window.roomsListTimer);
    window.roomsListTimer = setInterval(() => requestListRoom(socket), 2000);
    window.game.isHost = false;
    onEndGame();
    $("#unJoinedSection").show();
    $("#joinedSection").hide();
    $("#wait").hide();
    $("#roomDetail").hide();
    // $("#myBoard").hide();
}
function renderListRoom({length, list}){
    $("#listRoom").html("");
    for (let index = 0; index < length; index++) {
        $("#listRoom").append(createRoomElement(list[index]));
    }
}
function disableForNonHostPLayer(){
    console.log(`disableForNonHostPLayer`);
    $("#btnStartGame").prop("disabled", true);
    $("#time").prop("disabled", true);
}
function createRoomElement(room){
    return $($.parseHTML(`   <tr>
<th scope="row">${room.roomID}</th>
<td>${room.first}</td>
<td>${room.second}</td>
<td >

<button type="button" class="btn btn-primary" onClick="return joinRoom('${room.roomID}'); ">Join now</button>
</td>
</tr>`))
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
 function onStartGame(){
    $("#btnStartGame").prop("disabled", true);
    $("#btnSurrender").prop("disabled", false);
    $("#time").prop("disabled", true);
 }
 function sendStartGame(socket){
    let time = $("#time").val();
    window.game.time = time;
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
    $("#chat").val("");
    socket.emit("gameRoom", produceEvent(roomEvent.chat, msg));
 }
 function requestListRoom(socket){
    socket.emit("gameRoom", produceEvent(roomEvent.listRoom));
 }

let currentRoom = null;
window.roomsListTimer = null;
function onReady() {
  let token = localStorage.getItem("token");
  window.game = {};
  $.ajax({
    type: "GET", //GET, POST, PUT
    url: window.location.origin+'/user/me',  //the url to call
    beforeSend: function (xhr) {   //Include the bearer token in header
        xhr.setRequestHeader("Authorization", 'Bearer '+ token);
    }
}).done(function (response) {
    if (!response?.email){
        localStorage.removeItem("token");
        window.location.href = "/login.html" ;
    }else{
        window.game.current = response.nickname;
        $("#nickName").html(window.game.current);
    }
})
  $("#unJoinedSection").show();
  $("#joinedSection").hide();
  $("#wait").hide();
  $("#roomDetail").hide();
  $("#btnSurrender").prop("disabled", true);
  const socket = io(`ws://localhost:9092/chess?token=${token}`);
  initEvent(socket);
  $("#btnJoinRoom").on("click", () => {
    let roomID = $("#roomID").val();
    socket.emit("gameRoom", produceEvent(roomEvent.joinRoom, roomID));
  });
  $("#btnCreate").on("click", () => {
    socket.emit("gameRoom", produceEvent(roomEvent.createRoom, false));
  });
  $("#btnCreateBot").on("click", () => {
    socket.emit("gameRoom", produceEvent(roomEvent.createRoom, true));
  });
  $("#myBoard").show();
  $("#red").on("click", () => onSelectTeam(socket, 1));
  $("#black").on("click", () => onSelectTeam(socket, 0));
  $("#btnStartGame").on("click",() => sendStartGame(socket))
  $("#btnSurrender").on("click",() => sendSurrenderGame(socket))
  $("#btnExitRoom").on("click",() => sendExitRoom(socket))
  $("#btnChat").on("click",() => sendChat(socket))
  window.board = Xiangqiboard("myBoard", config);
  window.socket = socket;
}

function joinRoom(roomID){
    window.socket.emit("gameRoom", produceEvent(roomEvent.joinRoom, roomID));
    return false;
}