const EVENTNAME = "HST"
const HANDLERS = {}
let msgOrigin = "*"

/* server side */
export default (name, proc) => {
    if (HANDLERS[name]) {
        throw `${name} has used.`
    }
    HANDLERS[name] = proc
}

function setOrigin(origin) {
    msgOrigin = origin
}

function processMessage(event) {
    if (!Array.isArray(event.data) &&
        EVENTNAME !== event.data[0] &&
        4 !== event.data.length) {
        return;
    }

    const id = event.data[1];
    const cmd = event.data[2];
    const args = event.data[3];

    console.debug(`rcv event(${cmd}, ${id}, ${args})`)

    if (!HANDLERS[cmd]) {
        return;
    }
    const rs = HANDLERS[cmd](args)
    // 需要返回消息，并且不是返回的消息
    if (id && id !== cmd) {
        // promise
        if (rs && rs.then) {
            rs.then((d) => {
                event.source.postMessage([EVENTNAME, id, id, d], msgOrigin)
            })
        } else {
            event.source.postMessage([EVENTNAME, id, id, rs], msgOrigin)
        }
    }

}
window.addEventListener("message", processMessage, false)

/* client side */
function send2(target, name, args, proc) {
    if (target === window) {
        if (proc) {
            proc()
        }
        return
    }
    let id
    if (proc) {
        id = Math.random().toString(36).substr(2)
        HANDLERS[id] = function (d) {
            delete HANDLERS[id]
            proc(d)
        }
    }

    console.debug(`send event(${name}, ${id}, ${args})`)
    target.postMessage([EVENTNAME, id, name, args], msgOrigin)
}

function send(name, args, proc) {
    // if (window.parent === window) {
    //     if (proc) {
    //         proc()
    //     }
    //     return
    // }
    // let id
    // if (proc) {
    //     id = Math.random().toString(36).substr(2)
    //     HANDLERS[id] = function (d) {
    //         delete HANDLERS[id]
    //         proc(d)
    //     }
    // }

    // window.parent.postMessage([EVENTNAME, id, name, args])
    send2(window.parent, name, args, proc)
}

export {
    setOrigin, send, send2
}