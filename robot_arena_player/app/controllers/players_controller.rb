class PlayersController < ApplicationController
  before_filter :set_player_id, only: [:move, :fire, :stop]
  before_filter :set_direction, only: [:move, :fire]

  def index
  end

  def join
    session[:user] = params[:id]
    render :index
  end

  def move
    send_fact("playerMove")
  end

  def stop
    @direction = [0,0]
    send_fact("playerMove", { "direction" => @direction })
  end

  def fire
    send_fact("playerFire")
  end

  private

  def set_player_id
    @player_id ||= session[:user]
  end

  def set_direction
    @direction ||= params[:direction]
  end

  def send_fact(topic_name, options = {})
    uri       = get_uri_for_topic_name(topic_name)
    http      = Net::HTTP.new(uri.host, uri.port)
    @response = http.post(uri.path, build_params(options), json_headers)
  end

  def build_params(params = {})
    params["id"]        = params.fetch("id", @player_id)
    params["direction"] = params.fetch("direction", @direction)
    params["direction"].map! { |x| x.to_i }
    params.to_json
  end

  def get_uri_for_topic_name(topic_name)
    URI.parse("http://52.16.7.112:8000/topics/#{topic_name}/facts")
  end

  def json_headers
    { "Content-Type" => "application/json", "Accept" => "application/json" }
  end
end
