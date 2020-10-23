<template>
	<div class="chat-list">
		<a v-for="c of lists" :key="c.id" class="item" @click.prevent="chatTo(c)" href="#" :class="{ unread: c.t > -1 }">
			<i class="fa fa-user user-img"></i>
			<span class="name">{{ c.name }}</span>
			<span class="org">{{ c.orgname }}</span>
		</a>
		<chat class="wnd-chat" v-if="sessionId" :sessionId="sessionId" @close="sessionId = ''" />
	</div>
</template>

<script>
import { ajax, msg } from "@f/vendor"
import ChatBtn from "./btn"
import Chat from "./chat"
import "animate.css/source/attention_seekers/flash.css"

export default {
	components: { Chat, ChatBtn },
	data() {
		return { userid: "", sessionId: "", lists: [] }
	},
	mounted() {
		this.load()
	},
	methods: {
		async load() {
			let rs = await ajax.get("api/im/chats")
			this.lists = rs.chats
		},
		async chatTo(item) {
			if (this.sessionId && this.userid === item.uid) return
			this.sessionId = "";
			debugger;
			try {
				var data = await ajax.get(`api/im/auth/${item.uid}`)
				this.sessionId = data.id
				this.userid = item.uid
			} catch (err) {
				msg.info("连接即时通讯服务器出错，请重试", "error")
				console.error(err)
			}
		},
	},
}
</script>

<style lang="less" scoped>
.item {
	display: block;
	padding: 5px;

	&:hover {
		border-bottom: 1px solid;
	}
	.user-img {
		float: left;
		font-size: 200%;
		padding: 10px 10px;
	}

	.name {
		font-size: 120%;
	}

	.name:after {
		content: " ";
		display: block;
	}

	.org {
		clear: both;
	}

	&.unread {
		.user-img {
			animation: flash 2s infinite;
		}
	}
}

.wnd-chat {
	position: absolute;
	right: 10px;
	bottom: 10px;
	width: 300px;
}
.direct-chat-messages {
	height: 400px !important;
}
</style>
