// 扩展ES6的promise对象,增加always/fail函数,保持和jquery一致的接口
function init(t) {
    if (!t.Promise) {
        return;
    }

    if (t.Promise.prototype.fail === undefined) {
        t.Promise.prototype.fail = function (cb) {
            return this.catch(function (d) {
                return cb(d, undefined)
            })
        }
    }
    if (t.Promise.prototype.always === undefined) {
        t.Promise.prototype.always = function (cb) {
            return this.then(function (d) {
                return cb(d, undefined)
            }, function (e) {
                return cb(undefined, e)
            })
        }
    }
}

export default () => {
    init(window)
}