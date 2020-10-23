import { progress, msg, ajax } from '@f/vendor';

let _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

function encode(e) {
	var t = "";
	var n, r, i, s, o, u, a;
	var f = 0;
	e = _utf8_encode(e);
	while (f < e.length) {
		n = e.charCodeAt(f++);
		r = e.charCodeAt(f++);
		i = e.charCodeAt(f++);
		s = n >> 2;
		o = (n & 3) << 4 | r >> 4;
		u = (r & 15) << 2 | i >> 6;
		a = i & 63;
		if (isNaN(r)) {
			u = a = 64
		} else if (isNaN(i)) {
			a = 64
		}
		t = t + _keyStr.charAt(s) + _keyStr.charAt(o) + _keyStr.charAt(u) + _keyStr.charAt(a)
	}
	return t
}

function decode(e) {
	var t = "";
	var n, r, i;
	var s, o, u, a;
	var f = 0;
	e = e.replace(/[^A-Za-z0-9+/=]/g, "");
	while (f < e.length) {
		s = _keyStr.indexOf(e.charAt(f++));
		o = _keyStr.indexOf(e.charAt(f++));
		u = _keyStr.indexOf(e.charAt(f++));
		a = _keyStr.indexOf(e.charAt(f++));
		n = s << 2 | o >> 4;
		r = (o & 15) << 4 | u >> 2;
		i = (u & 3) << 6 | a;
		t = t + String.fromCharCode(n);
		if (u != 64) {
			t = t + String.fromCharCode(r)
		}
		if (a != 64) {
			t = t + String.fromCharCode(i)
		}
	}
	t = _utf8_decode(t);
	return t
}

function _utf8_encode(e) {
	e = e.replace(/rn/g, "n");
	var t = "";
	for (var n = 0; n < e.length; n++) {
		var r = e.charCodeAt(n);
		if (r < 128) {
			t += String.fromCharCode(r)
		} else if (r > 127 && r < 2048) {
			t += String.fromCharCode(r >> 6 | 192);
			t += String.fromCharCode(r & 63 | 128)
		} else {
			t += String.fromCharCode(r >> 12 | 224);
			t += String.fromCharCode(r >> 6 & 63 | 128);
			t += String.fromCharCode(r & 63 | 128)
		}
	}
	return t
}

function _utf8_decode(e) {
	var t = "";
	var n = 0;
	var r = 0;
	var c1 = 0;
	var c2 = 0;
	while (n < e.length) {
		r = e.charCodeAt(n);
		if (r < 128) {
			t += String.fromCharCode(r);
			n++
		} else if (r > 191 && r < 224) {
			c2 = e.charCodeAt(n + 1);
			t += String.fromCharCode((r & 31) << 6 | c2 & 63);
			n += 2
		} else {
			c2 = e.charCodeAt(n + 1);
			c3 = e.charCodeAt(n + 2);
			t += String.fromCharCode((r & 15) << 12 | (c2 & 63) << 6 | c3 & 63);
			n += 3
		}
	}
	return t
}

function clone(obj){
	var o;
    if (typeof obj == "object") {
        if (obj === null) {
            o = null;
        } else {
            if (obj instanceof Array) {
                o = [];
                for (var i = 0, len = obj.length; i < len; i++) {
                    o.push(clone(obj[i]));
                }
            } else {
                o = {};
                for (var j in obj) {
                    o[j] = clone(obj[j]);
                }
            }
        }
    } else {
        o = obj;
    }
    return o;
}

function isEmpty(value){
	if(value == null){
		return true;
	}else if(value == ""){
		return true;
	}else{
		return false;
	}
}

//JS加法函数
function add(a,b){
	var c, d, e;
    try {
        c = a.toString().split(".")[1].length;
    } catch (f) {
        c = 0;
    }
    try {
        d = b.toString().split(".")[1].length;
    } catch (f) {
        d = 0;
    }
    return e = Math.pow(10, Math.max(c, d)), (mul(a, e) + mul(b, e)) / e;
}

//JS减法函数
function sub(a,b){
	var c, d, e;
    try {
        c = a.toString().split(".")[1].length;
    } catch (f) {
        c = 0;
    }
    try {
        d = b.toString().split(".")[1].length;
    } catch (f) {
        d = 0;
    }
    return e = Math.pow(10, Math.max(c, d)), (mul(a, e) - mul(b, e)) / e;
}

//JS乘法函数
function mul(a, b) {
	if(!a){
		a = 0;
	}
	if(!b){
		b = 0;
	}
    var c = 0,
    d = a.toString(),
    e = b.toString();
    try {
        c += d.split(".")[1].length;
    } catch (f) {}
    try {
        c += e.split(".")[1].length;
    } catch (f) {}
    return Number(d.replace(".", "")) * Number(e.replace(".", "")) / Math.pow(10, c);
}

//JS除法函数
function div(a,b){
	var c, d, e = 0,
        f = 0;
    try {
        e = a.toString().split(".")[1].length;
    } catch (g) {}
    try {
        f = b.toString().split(".")[1].length;
    } catch (g) {}
    return c = Number(a.toString().replace(".", "")), d = Number(b.toString().replace(".", "")), mul(c / d, Math.pow(10, f - e));
}

function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}

function getSelect(e,canNull){
	var selecteds = e.rowState.selected;
	var datas = e.data.data;
	var k = 0;
	var srl;
	for(var i=0;i<selecteds.length;i++){
		if(selecteds[i]){
			k++;
			srl = i;
		}
	}
	if(k == 1){
		e.rowState.active = srl;
		return datas[srl];
	}else if(k == 0&&!canNull){
		msg.info("selectorder", "error");
	}else if(k >1){
		msg.info("onlyoneorder", "error");
	}
}

function getSelects(e){
	var selecteds = e.rowState.selected;
	var datas = e.data.data;
	var k = 0;
	var objs = [];
	for(var i=0;i<selecteds.length;i++){
		if(selecteds[i]){
			k++;
			objs.push(datas[i]);
		}
	}
	if(k >= 1){
		return objs;
	}else if(k == 0){
		msg.info("请选择", "error");
	}
}

function getUrlKey (name,url) {
	let letvalue = decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(url) || [, ""])[1].replace(/\+/g, '%20')) || null;
	//debugger;
	return letvalue;
}


export default {
	clone:function (obj) {
	    return clone(obj);
	},
	isEmpty:function(value){
		return isEmpty(value);
	},
	add:function(a,b){
		return add(a,b);
	},
	sub:function(a,b){
		return sub(a,b);
	},
	mul:function(a,b){
		return mul(a,b);
	},
	div:function(a,b){
		return div(a,b);
	},
	uuid:function(){
		return uuid();
	},
	getSelect:function(e,canNull){
		return getSelect(e,canNull);
	},
	getSelects:function(e){
		return getSelects(e);
	},
	base64encode:function(e){
		return encode(e);
	},
	base64decode:function(e){
		return decode(e);
	},
	getUrlKey:function(name,url){
		return getUrlKey(name,url);
	},


}
