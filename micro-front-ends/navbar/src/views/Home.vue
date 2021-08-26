<template>
  <ul class="navbar-nav">
    <li
      v-for="(item, index) in items"
      :key="index"
      class="nav-item d-none d-sm-inline-block"
    >
      <router-link v-bind:to="item.link" class="nav-link">{{
        item.name
      }}</router-link>
    </li>
    <li class="nav-item d-none d-sm-inline-block">
      <router-link to="/app2/about" class="nav-link">关于</router-link>
    </li>
  </ul>
</template>

<script>
export default {
  data() {
    return {
      items: [
        {
          name: "nav 应用1",
          link: "/app1",
        },
        {
          name: "nav 应用2",
          link: "/app2",
        },
      ],
    };
  },
  computed: {},
  created() {},
  mounted() {},
  methods: {},
};
</script> 