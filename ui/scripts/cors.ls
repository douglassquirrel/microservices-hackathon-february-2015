@createCORSRequest = (method, url) ->
  xhr = new XMLHttpRequest()
  if "withCredentials" of xhr

    # Check if the XMLHttpRequest object has a "withCredentials" property.
    # "withCredentials" only exists on XMLHTTPRequest2 objects.
    xhr.open(method, url, true)

  else if typeof XDomainRequest != "undefined"

    # Otherwise, check if XDomainRequest.
    # XDomainRequest only exists in IE, and is IE's way of making CORS requests.
    xhr = new XDomainRequest()
    xhr.open(method, url)

  else

    # Otherwise, CORS is not supported by the browser.
    xhr = null

  
  xhr
