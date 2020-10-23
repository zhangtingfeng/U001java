import "@f/vendor"
import Vue from 'vue'
import App from "./app"
import { i18n } from '@f/framework'

new Vue({
	el: '#app',
	i18n,
	template: '<app/>',
	components: {
		App
	}
})