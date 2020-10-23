<template>
	<section v-if="paths.length">
		<ol class="breadcrumb">
			<li v-for="(m) of paths" v-bind:key="m.data.id">
				<a href="#" @click.prevent>
					<i v-if="m.data.icon" :class="`fa fa-${m.data.icon}`"></i>
					{{$t(`menus.${m.data.id}`)}}
				</a>
			</li>
		</ol>
	</section>
</template>

<script>
import menu from "../data/menu";
export default {
  data() {
    return {
      menu: menu.data,
      paths: []
    };
  },
  methods: {
    load() {
      let paths = [];
      var m = menu.get(this.menu.curr);
      while (m != null) {
        paths.push(m);
        m = m.parent ? menu.get(m.parent.data.id) : null;
      }
      this.paths = paths.reverse();
    }
  },
  watch: {
    "menu.curr"() {
      this.load();
    }
  },
  mounted(){
    this.load()
  }
};
</script>

<style scoped>
.breadcrumb {
  margin: 0;
  padding: 9px 
}
</style>
