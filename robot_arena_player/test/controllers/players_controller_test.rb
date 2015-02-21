require 'test_helper'

class PlayersControllerTest < ActionController::TestCase
  test "should get move" do
    get :move
    assert_response :success
  end

  test "should get fire" do
    get :fire
    assert_response :success
  end

end
