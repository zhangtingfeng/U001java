<template>
	<div>
		<a href="#" class="btn btn-link" @click.prevent="test1">Start</a>
		<a href="#" class="btn btn-link" @click.prevent="test2">Close</a>
	</div>
</template>

<script>
import { ajax } from "@f/vendor"
export default {
	data() {
		return { ws: null }
	},
	mounted() {},
	methods: {
		test1() {
			debugger;
			this.ws = new WebSocket(`ws://${window.location.host}/im/chat?${this.chatSessionId}`)
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
		},
		test2() {
			if (this.ws) {
				this.ws.close()
				this.ws = null
			}
		},
	},
}
</script>
