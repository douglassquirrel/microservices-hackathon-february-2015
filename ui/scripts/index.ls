
# "http://52.16.7.112:8000"
combo = new Combo("http://52.16.7.112:8000")

combo.use ["state", "ArenaClock"]

combo.listen "state", (data) ->


clockElem = document.getElementById("clock")
combo.listen "ArenaClock", (data) ->
	clockElem.replaceChild(document.createTextNode(data.tick), clockElem.firstChild)


boardSize = [20, 20]
cellSize = 20

arenaElem = document.getElementById("arena")
paper = Raphael(400, 100, boardSize.0*cellSize, boardSize.1*cellSize)

testData = {
	"moves": [
		{
		"id": "player1",
		"coordinates": [
		1,
		1
		]
		},
		{
		"id": "player2",
		"coordinates": [
		10,
		10
		]
		},
		{
		"id": "player3",
		"coordinates": [
		15,
		15
		]
		}
	]
}

draw = (data) ->
	arenaElem.textContent = JSON.stringify(data, null, "  ")

	paper.clear()
	paper.rect(0, 0, boardSize.0*cellSize, boardSize.1*cellSize).attr("fill", '#000')

	for robot in data.moves
		pos = robot.coordinates
		console.log pos
		paper.rect(pos.0*cellSize, pos.1*cellSize, cellSize, cellSize).attr("fill", '#f00')

combo.listen "state", draw
draw(testData)
