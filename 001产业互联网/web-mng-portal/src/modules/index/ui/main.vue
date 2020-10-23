<template>
	<div class="content-wrapper">
		<main-header/>
		<main-content/>
	</div>
</template>

<script>
import MainHeader from "./MainHeader";
import MainContent from "./MainContent";
export default {
  components: {
    MainHeader,
    MainContent
  }
};
</script>
