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
}
function initGameRoomEvent(response){
    console.log(response);
    switch (response.type) {
        case roomEvent.joinRoom:
            $("#currentRoom").val(response.roomID);
            $("#wait").show();
            break;
        default:
            break;
    }
}
const roomEvent = {
    "joinRoom":1
}
function produceEvent(eventType, data){
    return {
        type:eventType,
        data
    }
}
let currentRoom = null
function onReady() {
  let userID = localStorage.getItem("token");
  if (!userID) {
    userID = "user" + Math.floor(Math.random() * 1000 + 1);
    localStorage.setItem("token", userID);
  };
  $("#userID").html(userID);
  $("#currentRoom").hide();
  $("#wait").hide();
  const socket = io(`ws://localhost:9092/chess?token=${userID}`);
  initEvent(socket);
  $("#btnJoinRoom").on("click", () => {
    let roomID = $("#roomID").val();
    socket.emit("gameRoom", produceEvent(roomEvent.joinRoom, roomID));
  });
  $("#myBoard").hide();
  const board = Xiangqiboard("myBoard", config);
}



