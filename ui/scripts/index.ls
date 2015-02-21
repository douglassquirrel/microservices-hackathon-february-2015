
# "http://52.16.7.112:8000"
combo = new Combo("http://52.16.7.112:8000")

combo.use ["state", "ArenaClock"]

combo.listen "state", (data) ->


clockElem = document.getElementById("clock")
combo.listen "ArenaClock", (data) ->
	clockElem.replaceChild(document.createTextNode(data.tick), clockElem.firstChild)
