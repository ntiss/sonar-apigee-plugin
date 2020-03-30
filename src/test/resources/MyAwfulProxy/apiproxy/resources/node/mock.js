var http = require('http');

console.log('node.js application starting...');

var svr = http.createServer(function(req, resp) {
    
    resp.setHeader('Content-Type', 'application/json');
    resp.writeHead(200);

    if(req.url.match(/^\/todos/)) {
        resp.end('[{"id":1,"details":"Change the lightbulb in the kitchen"},{"id":2,"details":"Take out the trash"}]');

    } else {
        resp.end('{"label" : "Nothing here", "status":"NOT_FOUND"}');
    }

    
});



svr.listen(9000, function() {
    console.log('Node HTTP server is listening');
});