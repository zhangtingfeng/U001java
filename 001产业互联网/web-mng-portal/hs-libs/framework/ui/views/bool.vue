<template>
  <i class="fa" v-if="undefined != value" :class="{'fa-check': value, 'text-success': value, 'fa-circle': !value,'text-danger': !value}"></i>
</template>

<script>
export default {
  props: ["value"]
};
</script>

