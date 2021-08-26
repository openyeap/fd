const render = $ => {
  $('#app3').html('Hello, render with jQuery');
  // alert($('#app3').html());
  return Promise.resolve();
};

(global => {
  global['app3'] = {
    bootstrap: (props) => {
      console.log('purehtml bootstrap', props);
      return Promise.resolve();
    },
    mount: (props) => {
      console.log('purehtml mount', props);
      return render($);
    },
    unmount: (props) => {
      console.log('purehtml unmount',props);
      $('#app3').html('');
      return Promise.resolve();
    },
    update:(props)=>{
      console.log('purehtml update',props);      
      return Promise.resolve();
    }
  };
})(window);
