<template>
	<span>{{text}}</span>
</template>

<script>
export default {
	data(){
		return {text: this.value ? new Date(this.value).format("YYYY-MM-DD"):""};
	},
  props: ["value"]
};
</script>
