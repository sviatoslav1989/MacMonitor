$.tablesorter.addParser({
    // set a unique id
    id: 'ifName',
    is: function(s) {
        // return false so this parser is not auto detected
        return false;
    },
    format: function(s) {
        var arr = s.match( /([a-zA-Z]+)([0-9]+)(\/([0-9]+)(\/([0-9]+)){0,1}){0,1}/i);

        // console.log(arr);
        if(arr[2]==undefined){
            arr[2]=0;
        }
        if(arr[4]==undefined){
            arr[4]=0;
        }

        if(arr[6]==undefined){
            arr[6]=0;
        }
        var result = (parseInt(arr[2])*10000+parseInt(arr[4])*100+parseInt(arr[6])*1);
        //console.log(result);
        return result;
    },
    // set type, either numeric or text
    type: 'numeric'
});

$.tablesorter.addParser({
    id: "myIpAddress",
    is: function (s) {
        return false;
    }, format: function (s) {
        var a = s.split("."),
            r = "",
            l = a.length;
        for (var i = 0; i < l; i++) {
            var item = a[i];
            if (item.length == 2) {
                r += "0" + item;
            }
            else if (item.length == 1) {
                r += "00" + item;
            }
            else {
                r += item;
            }
        }
        return $.tablesorter.formatFloat(r);
    }, type: "numeric"
});

$.tablesorter.addParser({
    id: "customDate",
    is: function(s) {
        return false;
    },
    format: function(s) {
        s = s.replace(/\-/g," ");
        s = s.replace(/:/g," ");
        s = s.replace(/\./g," ");
        s = s.replace(/\//g," ");
        s = s.split(" ");
        return $.tablesorter.formatFloat(new Date(s[2]+2000, s[1]-1, s[0], s[3], s[4]).getTime());
    },
    type: "numeric"} );

$.tablesorter.addParser({
    id: "customDateWithSeconds",
    is: function(s) {
        return false;
    },
    format: function(s) {
        s = s.replace(/\-/g," ");
        s = s.replace(/:/g," ");
        s = s.replace(/\./g," ");
        s = s.replace(/\//g," ");
        s = s.split(" ");
        return $.tablesorter.formatFloat(new Date(s[2]+2000, s[1]-1, s[0], s[3], s[4], s[5]).getTime());
    },
    type: "numeric"} );