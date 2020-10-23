<template>
	<div class="box direct-chat draggable-source">
		<div class="box-header with-border bg-gray">
			<h3 class="box-title" v-if="state === 2">{{ chatCtx.user2.name }}({{ chatCtx.user2.orgName }})</h3>
			<h3 class="box-title" v-else>即时通讯</h3>

			<div class="box-tools pull-right">
				<button type="button" class="btn btn-box-tool" data-widget="remove" @click.prevent="close">
					<i class="fa fa-times"></i>
				</button>
			</div>
		</div>
		<div class="box-body" v-if="state === 0">连接中...</div>

		<template v-else>
			<div class="box-body">
				<div class="direct-chat-messages">
					<div v-if="hasMore" class="text-center">
						<a href="#" @click.prevent="loadMore">
							<i v-if="loadingMore" class="fa fa-spin fa-circle-o-notch"></i>
							加载更多
						</a>
					</div>
					<template v-for="m of msgs">
						<div class="direct-chat-msg right" :key="m.id" v-if="m.userid === chatCtx.user.id">
							<div class="direct-chat-info clearfix">
								<span class="direct-chat-name pull-right">我</span>
								<span class="direct-chat-timestamp pull-left">{{ m.dt }}</span>
							</div>
							<i class="direct-chat-img fa fa-user"></i>
							<div class="direct-chat-text">{{ m.msg }}</div>
						</div>
						<div class="direct-chat-msg" :key="m.id" v-else>
							<div class="direct-chat-info clearfix">
								<span class="direct-chat-name pull-left">{{ chatCtx.user2.name }}</span>
								<span class="direct-chat-timestamp pull-right">{{ m.dt }}</span>
							</div>
							<i class="direct-chat-img fa fa-user"></i>
							<div class="direct-chat-text">{{ m.msg }}</div>
						</div>
					</template>
				</div>
			</div>

			<div class="box-footer">
				<form action @submit.prevent="send">
					<div class="input-group">
						<input type="text" name="message" placeholder="输入消息" class="form-control" v-model="form.msg" />
						<span class="input-group-btn">
							<button type="submit" class="btn btn-success btn-flat">
								<i v-if="sending" class="fa fa-spin fa-circle-o-notch"></i>
								发送
							</button>
						</span>
					</div>
				</form>
			</div>
		</template>
	</div>
</template>

<script>
import { ajax } from "@f/vendor"
import chat from "./chatProc"
import { Draggable } from "@shopify/draggable"

export default {
	props: ["sessionId"],
	data() {
		return {
			chatCtx: null,
			sending: false,
			state: 0,
			chat: null,
			form: { msg: "" },
			msgs: [],
			loadingMore: false,
			hasMore: true,
			dragObj: {},
		}
	},

	mounted() {
		this.init()
	},
	unmounted() {
		this.close()
	},

	methods: {
		init() {
			this.chat = new chat(this.sessionId)
			this.chat.on("state", (cmd, state) => this.onState(cmd, state))
			this.chat.on("init", (cmd, data) => this.onInit(cmd, data))
			this.chat.on("chat2", (cmd, data) => this.onChat2(cmd, data))
			this.chat.init()

			this.dragObj.draggable = new Draggable(this.$el, { handle: "div.box-header" })
			this.dragObj.draggable.on("drag:start", this.doDragStart)
			this.dragObj.draggable.on("drag:move", this.doDragMove)
			this.dragObj.draggable.on("drag:stop", this.doDragStop)
		},
		close() {
			this.chat.uninit()
			this.$emit("close")
			this.dragObj.draggable.destroy()
		},
		send() {
			if (this.sending) return
			if (!this.form.msg) return
			this.chat.on("rs", (cmd, data) => {
				this.sending = false
				this.msgs.push(data)
				this.form.msg = ""
				this.scroll(1)
			})
			this.chat.send({
				type: 0,
				userid: this.chatCtx.user.uid,
				msg: this.form.msg,
			})
		},

		scroll(type) {
			this.$nextTick(() => {
				let $el = $(this.$el).find("div.direct-chat-messages")
				if (type === 0) {
					$el.scrollTop(0)
				} else {
					$el.scrollTop($el[0].scrollHeight)
				}
			})
		},

		async loadMore() {
			if (this.loadingMore) return
			try {
				this.loadingMore = true
				let url = `api/im/msgs/${this.chatCtx.chatId}`
				if (this.msgs.length > 0) {
					url = `${url}?id=${this.msgs[0].id}`
				}
				let data = await ajax.get(url)
				this.hasMore = data.data.length === 10
				for (let m of data.data) {
					this.msgs.splice(0, 0, m)
				}
			} catch {
				this.hasMore = false
			} finally {
				this.loadingMore = false
			}
		},

		onState(cmd, state) {
			this.state = state
		},

		async onInit(cmd, data) {
			this.state = 2
			this.chatCtx = data
			await this.loadMore()
			this.scroll(1)
		},

		onChat2(cmd, data) {
			this.msgs.push(data)
			this.scroll(1)
		},

		doDragStart(event) {
			this.dragObj.pos1 = { x: event.originalEvent.clientX, y: event.originalEvent.clientY }
		},

		doDragMove(event) {
			this.dragObj.pos2 = { x: event.originalEvent.clientX, y: event.originalEvent.clientY }
		},

		doDragStop(event) {
			if (this.dragObj.pos2.y < 0) return

			let bottom = parseInt($(this.$el).css("bottom"))
			let right = parseInt($(this.$el).css("right"))

			right += this.dragObj.pos1.x - this.dragObj.pos2.x
			bottom += this.dragObj.pos1.y - this.dragObj.pos2.y

			$(this.$el).css({ right, bottom })
		},
	},
}
</script>

<style lang="less" scoped>
.direct-chat-messages {
	height: 400px;
}
i.fa.direct-chat-img {
	font-size: 24px;
	padding: 5px 10px;
	color: greenyellow;
}
.box-header {
	cursor: move;
}

.right {
	i.fa.direct-chat-img {
		color: green;
	}
}
</style>
