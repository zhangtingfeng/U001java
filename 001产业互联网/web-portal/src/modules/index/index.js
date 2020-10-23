import { i18n } from "@f/framework"
import Vue from 'vue'
import "@m/common"
import App from "./app"
import user from "@/apps/user";

user.init()

new Vue({
	el: '#app',
	i18n,
	template: '<app/>',
	components: {
		App
	}
}) 