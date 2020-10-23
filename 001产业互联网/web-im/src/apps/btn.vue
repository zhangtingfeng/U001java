<template>
	<span>
		<a href="#" @click.prevent.stop="chat">
			<img :src="img" />
		</a>
		<chat class="wnd-chat" v-if="sessionId" :sessionId="sessionId" @close="sessionId = ''" />
	</span>
</template>

<script>
import { ajax, msg } from "@f/vendor"

import chat from "./chat"

export default {
	components: { chat },
	props: ["userid"],
	data() {
		return {
			img: "imgs/icon_service_highlight.svg",
			sessionId: null,
		}
	},
	methods: {
		async chat() {
			try {
				var data = await ajax.get(`api/im/auth/${this.userid}`)
				this.sessionId = data.id
			} catch (err) {
				msg.info("连接即时通讯服务器出错，请重试", "error")
				console.error(err)
			}
		},
	},
}
</script>

<style lang="less" scoped>
.wnd-chat {
	position: fixed;
	right: 10px;
	bottom: 10px;
	width: 300px;
}
.direct-chat-messages {
	height: 400px !important;
}
</style>
