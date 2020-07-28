
var FAIL_MESSAGE = '<span class="fail">FAIL</span>';
var PASS_MESSAGE = '<span class="pass">PASS</span>';

var assertTrue = function(fn) {
  try {
    if (fn()) {
      return PASS_MESSAGE;
    }
    return FAIL_MESSAGE;
  }
  catch(err) {
    return '<span class="fail">Error: '+ (err.message || err) + '</span>';
  }
};
var assertFalse = function(fn) {
  try {
    if (fn()) {
      return FAIL_MESSAGE;
    }
    return PASS_MESSAGE;
  }
  catch(err) {
    return '<span class="fail">Error: '+ (err.message || err) +'</span>';
  }
};
