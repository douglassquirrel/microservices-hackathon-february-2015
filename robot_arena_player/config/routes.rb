Rails.application.routes.draw do
  get "players/index"
  get "players/move"
  get "players/stop"
  get "players/fire"
  
  post "players/join"

  get "welcome/index", to: "welcome#index"
  root "welcome#index"
end
