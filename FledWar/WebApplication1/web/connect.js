

var debug = function(message) {
    $("#messages").append(message+"<br />");
}
$(window).load(function() {
    connection.init();
    
});

var connection = {
  
    socket : undefined,
    
    session_id : undefined,
    
    username : undefined,
  
    init : function() {
        
//        loading_status.start();
//        
//        var result;
//        $.ajax({
//            url : "/fledclient/game/start",
//            cache : false,
//            dataType : "json",
//            async : false
//        }).done(function (data) {
//            result = data;
//        }).fail(function(jqXHR, textStatus){
//            throw testStatus;
//        });
//
//        if (result.result == "unsuccessful") {
//            debug("unable to get connection location");
//            throw "unable to get connection location";
//        }
//
//        this.session_id = result.session_id;
//        this.username = result.username;
//
//        debug("session id: " + this.session_id);
//        debug("username: " + this.username);
        
        
        
        this.init_connection("localhost:8080/webapp/socket");
    },
    
    init_connection : function(host) {

        var connecting = "ws://"+host;
        debug("connecting to: " + connecting);
        
        var WebSocketObject = window.WebSocket || window.MozWebSocket;
        if (!WebSocketObject){
            error_message.notice("<p>Your browser does not support WebSocket.</p>");
            return;
        }

        this.socket = new WebSocketObject(connecting);
		this.socket.onopen      = this._socket_onopen;
        this.socket.onmessage   = this._socket_onmessage;
		this.socket.onclose     = this._socket_onclose;
		this.socket.onerror     = this._socket_onerror;
        
//        interface WebSocket { 
//            readonly attribute DOMString URL; 
//            // ready state 
//            const unsigned short CONNECTING = 0; 
//            const unsigned short OPEN = 1; 
//            const unsigned short CLOSED = 2; 
//            readonly attribute unsigned short readyState; 
//            readonly attribute unsigned long bufferedAmount;  
//
//            // networking 
//            attribute Function onopen; 
//            attribute Function onmessage; 
//            attribute Function onclose; 
//            boolean send(in DOMString data); 
//            void close(); 
//            };
//        }
        debug("ready state: " + this.socket.readyState);
    },
    
    _socket_onopen : function() {
        connection.send_message({
            action : "init",
            session_id : connection.session_id,
            username : connection.username
        });
        debug("opened");
//        loading_status.stop();
    },
    
    _socket_onmessage : function(event) {
        debug("message: " + event.data);
    },
    
    _socket_onclose : function(event) {
        // event.wasClean
        // event.code
        // event.reason
//        error_message.notice("<p>lost connection [1]</p>");
        debug(event);
    },
    
    _socket_onerror : function() {
//        error_message.notice("<p>lost connection [2]</p>");
        debug(event);
    },
    
    send_message : function(object) {
        var message = JSON.stringify(object);
        debug(message);
        connection.socket.send(message);
    },
    
    close : function() {
        if (this.socket != null) {
            this.socket.onopen = null;
            this.socket.onclose = null;
            this.socket.onerror = null;
            this.socket.onmessage = null;
            this.socket.close();
        }
    }
  
};
