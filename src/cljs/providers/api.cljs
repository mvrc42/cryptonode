(ns providers.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [app.state :refer [app-state]]))

(def cur-available ["USD" "EUR" "GBP" "BTC"])
(defn cur-symbol [c]
  (case c
        "USD" "$"
        "EUR" "€"
        "GBP" "£"
        "BTC" "฿"
        ""))

(def ^:private url "https://318h5of2kh.execute-api.eu-west-1.amazonaws.com/dev/currencies")

(defn update-data
  "Load masternodes data from the api"
  []
  (go (let [response (<! (http/get url {:with-credentials? false}))]
    (when (:success response)
      (swap! app-state assoc-in [:api-data] (get response :body))))))

(defn get-currency [c]
  (->> (get @app-state :api-data)
       (filter #(= (get % :coin) c))
       first))
