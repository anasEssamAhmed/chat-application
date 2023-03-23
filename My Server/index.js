var socketIO = require('socket.io'),
    http = require('http'),
    port = process.env.PORT || 8080,
    ip = process.env.IP || '192.168.1.129', //My IP address. I try to "127.0.0.1" but it the same => don't run
    server = http.createServer().listen(port, ip, function() {
        console.log("IP = " , ip);
        console.log("start socket successfully");
});
var arrayUserName = []; // this is array to insert on the email and password and name.
var arrayUserGroup = [];
io = socketIO.listen(server);
//io.set('match origin protocol', true);
io.set('origins', '*:*');

var run = function(socket){
socket.broadcast.emit("message", "hello");
    socket.on("message", function(id , value) {
        console.log(value);
        io.emit("message" , id , value)
    });

    socket.on("user-join", function(value) {
        console.log(value + "user-join");
        socket.broadcast.emit("new-users", value);

    });

    socket.on("signUp", function(id , value) {
        if(arrayUserName.length > 0) {
            for(let i = 0 ; i < arrayUserName.length ; i++) {
                if(arrayUserName[i]['email'] == value['email']) {
                    io.emit("signUp" , id , false , value);
                    break;
                }else { 
                    console.log("This is value : " + value)
                    arrayUserName.push(value);
                    io.emit("signUp" , id , true , value);
                    break;
                   //  console.log(arrayUserName + " signUp");
                }
            }  
        }else { 
        console.log("This is value : " + value)    
        arrayUserName.push(value);
        io.emit("signUp" , id , true , value);
        } 
    });
    socket.on("SignIn", function( id , value) {
        for(let i = 0 ; i < arrayUserName.length ; i++){
            if(arrayUserName[i]['email'] == value['emailToSignIn'] && arrayUserName[i]['password'] == value['passwordToSignIn']) {
                io.emit("SignIn" , id ,true , arrayUserName[i]);
                console.log("ID : " , arrayUserName[i]['id'] , " Name : "  , arrayUserName[i]['name']);
                break;
            }else {
                io.emit("SignIn" , id , false);
            }
        }
    });
    socket.on("userInfo", function(value) {
        io.emit("userInfo" , arrayUserName);
    });

    socket.on("GroupInfo", function(value) {
        arrayUserGroup.push(value);
        io.emit("allGroups" , arrayUserGroup);
        console.log("This is Group info : " + arrayUserGroup);

    });
    socket.on("allGroups", function(value) {
        io.emit("allGroups" , arrayUserGroup);

    });
    socket.on("GroupMassage", function(id , value) {
        console.log(value);
        console.log("My Test Massage Group");
        io.emit("GroupMassage" , id , value);
    });
    socket.on("updateCurrent", function(value) {
        for(let i = 0 ; i < arrayUserName.length ; i++ ) {
            if(arrayUserName[i]['id'] == value['id']) {
                arrayUserName[i]['isTrue'] = value['isTrue'];
                io.emit("userInfo" , arrayUserName);
                break;
            }
        }
    });
    
}

io.sockets.on('connection', run);