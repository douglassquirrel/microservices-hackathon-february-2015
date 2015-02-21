# Place all the behaviors and hooks related to the matching controller here.
# All this logic will automatically be available in application.js.
# You can use CoffeeScript in this file: http://coffeescript.org/

move = (x,y) ->
  data = { direction: [x, y] }
  callback = (response) -> console.log response
  $.get '/players/move', data, callback, 'json'
  console.log "Move: #{x}, #{y}"

moveTop    = -> move(0,1)
moveRight  = -> move(1,0)
moveBottom = -> move(0,-1)
moveLeft   = -> move(-1,0)
stop       = -> move(0,0)

fire = (x,y) ->
  data = { direction: [x, y] }
  callback = (response) -> console.log response
  $.get '/players/fire', data, callback, 'json'
  console.log "Fire: #{x}, #{y}"

fireTop    = -> fire(0,1)
fireRight  = -> fire(1,0)
fireBottom = -> fire(0,-1)
fireLeft   = -> fire(-1,0)


$ ->

  # MOVE
  $('#move-top').on 'click', (event) ->
    event.preventDefault()
    moveTop()
    
  $('#move-right').on 'click', (event) ->
    event.preventDefault()
    moveRight()
      
  $('#move-bottom').on 'click', (event) ->
    event.preventDefault()
    moveBottom()

  $('#move-left').on 'click', (event) ->
    event.preventDefault()
    moveLeft()

  $('#stop').on 'click', (event) ->
    event.preventDefault()
    stop()

  # FIRE
  $('#fire-top').on 'click', (event) ->
    event.preventDefault()
    fireTop()
    
  $('#fire-right').on 'click', (event) ->
    event.preventDefault()
    fireRight()
      
  $('#fire-bottom').on 'click', (event) ->
    event.preventDefault()
    fireBottom()

  $('#fire-left').on 'click', (event) ->
    event.preventDefault()
    fireLeft()
  
  # KEYBOARD
  document.onkeydown = (e) ->
    switch e.keyCode
      when 32 then stop()
      
      when 37 then moveLeft()
      when 38 then moveTop()
      when 39 then moveRight()
      when 40 then moveBottom()

      when 65 then fireLeft()
      when 87 then fireTop()
      when 68 then fireRight()
      when 83 then fireBottom()