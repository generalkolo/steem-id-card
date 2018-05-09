const express = require('express');  
const bodyParser = require('body-parser');  
const steem = require('steem');

let app = express();  
var urlencodedParser = bodyParser.urlencoded({extended : false});

// Function to handle the root path
app.post('/postdata',urlencodedParser ,async  function(req, res) {

    let username = req.body.username;

    var whatIsThis = function () {
    showOff(username)
    .then(function (fulfilled) {
            res.send(fulfilled);
        })
        .catch(function (error) {
            console.log(error.message);
        });
};
    whatIsThis();

});

let server = app.listen(8080, function() {  
    console.log('Server is listening on port 8080');
});

var showOff = function (account) {
   try {
        return new Promise(
            function (resolve, reject) {
                var reply;

                    steem.api.getAccounts([account], function(err, result) {
                        if (result.length == 0) {
                            reply = "This user doesnt exist";
                            resolve(reply);
                        }
                        else{
                            var meta = result[0].json_metadata.split("\",\"");
                            var name,image,location,id,date_joined = null;

                            var rank = steem.formatter.reputation(result["0"].reputation);
                            id = result[0].id;
                            date_joined = result[0].created.substring(0,10);

                          for(i = 0; i < meta.length ; i++){
                            if (meta[i].includes("profile_image\""))
                              image = meta[i];
                            if (meta[i].includes("name\""))
                              name = meta[i];
                            if (meta[i].includes("location\""))
                              location = meta[i];
                        }

                        if (image == null)
                            image = "Unkown";
                        else
                            image = image.substring(image.indexOf("http"));

                        if (name == null)
                            name = "Unkown";
                        else
                            name = name.substring(name.indexOf("\":\"") + 3);

                        if (location == null)
                            location = "Unkown";
                        else
                            location = location.substring(location.indexOf("\":\"") + 3);

                        reply = {"rank":rank,"image":image,"name":name,"location":location,"id":id,"date_joined":date_joined};
                        
                        resolve(reply);
                        }
                    });
            }
        );
      }
    catch (error) {
        console.log(error.message);
    }
};