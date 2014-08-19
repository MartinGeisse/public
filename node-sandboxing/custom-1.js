

// cannot directly return an object, only a primitive value, so we JSON-encode the
// result and decode it on the host side. Node-Sandbox also adds quotes which
// we have to remove
JSON.stringify({"responseBody": "foo'bar", "iquehfiuq": "wurst"});
