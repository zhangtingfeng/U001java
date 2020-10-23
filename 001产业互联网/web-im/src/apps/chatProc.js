class ChatProc {
	constructor(chatSessionId) {
		this.chatSessionId = chatSessionId
		this.state = 0
		this.events = {}
	}

	init() {
		debugger;
		let letHost=window.location.host;
		if (letHost.indexOf('localhost')>-1)
		{letHost="127.0.0.1:18008";}
		let webSocket=`ws://${letHost}/im/chat?${this.chatSessionId}`;
		this.ws = new WebSocket(webSocket)
		this.ws.onopen = () => {
			this.state = 1
			this.fire("state", this.state)
		}
		this.ws.onclose = () => {
			this.state = -1
			this.fire("state", this.state)
		}
		this.ws.onmessage = (evt) => {
			let msg = JSON.parse(evt.data)
			try {
				if (msg.data) {
					this.fire(msg.cmd, JSON.parse(msg.data))
					console.log(msg)
				}
			} catch (err) {
				console.error(`${err}: ${msg.data}`)
			}
		}
	}

	uninit() {
		this.ws.close()
	}

	async send(msg) {
		if (this.state === -1) {
			this.init()
			setTimeout(() => {
				this._send(msg)
			}, 100)
		} else this._send(msg)
	}

	_send(msg) {
		this.ws.send(JSON.stringify({ cmd: "chat", data: JSON.stringify(msg) }))
	}

	on(cmd, proc) {
		this.events[cmd] = proc
	}

	fire(cmd, data) {
		if (this.events[cmd]) {
			this.events[cmd](cmd, data)
		} else {
			console.warn(`Unknown cmd [${cmd}]`)
		}
	}
}

export default ChatProc
