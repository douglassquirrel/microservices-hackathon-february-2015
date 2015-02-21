@Combo = class
	(@url) ~>
		@subs = {}
		@buffers = {}
	request: (method, path, headers, data, callback) ->
		dataBuffer = JSON.stringify(data)

		req = createCORSRequest method, @url + path
		req.setRequestHeader 'Content-Type', 'application/json'

		if headers
			for k, v of headers
				req.setRequestHeader k, v

		req.onload = ->
			result = req.responseText

			if req.status >= 200 and req.status < 300
				console.log("Success: #{path}")
				if callback
					callback (result && JSON.parse(result))
			else
				console.log(JSON.stringify(data))
				console.log("Error: #{req.status} for #{method} #{path}")

		if data
			req.send dataBuffer
		else
			req.send!

	list: (callback) ->
		@request 'GET', '/topics', null, null, callback

	publish: (topic_name, after_id, fact, callback) ->
		@request 'POST', "/topics/#{topic_name}/facts", null, fact, callback

	get: (topic_name, after_id, callback) ->
		if after_id
			after_id = "?" + after_id
		else
			after_id = ""
		@request 'GET', "/topics/#{topic_name}/facts" + after_id, null, null, callback

	subscribe: (topic_name, callback) ->
		console.log "Subscribing to #topic_name..."
		@request 'POST', "/topics/#{topic_name}/subscriptions", null, null, callback

	next: (topic_name, subscription_id, timeout, callback) ->
		@request 'GET', "/topics/#{topic_name}/subscriptions/#{subscription_id}/next", {"Patience": timeout}, null, callback

	use: (topic_names) ->
		for let topic_name in topic_names
			@buffers[topic_name] = []

			result <~ @subscribe topic_name, _
			console.log "Subscription succeeded: #result"
			subscription_id = result.subscription_id

			console.log "Running worker..."

			do run = ~>
				item <~ @next topic_name, subscription_id, 30, _
				if item
					if @subs[topic_name]
						@subs[topic_name](item)
					else
						@buffers[topic_name].push item
				run()

	listen: (topic_name, callback) ->
		@subs[topic_name] = callback
		for item in @buffers[topic_name]
			callback item
		@buffers[topic_name] = []

	unlisten: (topic_name) ->
		@subs[topic_name] = null
