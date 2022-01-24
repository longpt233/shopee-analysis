import requests 
import json
import pandas as pd
from tqdm import tqdm
def get_item_shop_info(match_id,newest):
    url_search_item = 'https://shopee.vn/api/v4/search/search_items?by=relevancy&limit=60&match_id={}&newest={}'
    
    def get_item_shop_id(data):
        item_id = data['item_basic']['itemid']
        shop_id = data['item_basic']['shopid']
        return {
            'itemid':item_id,
            'shopid':shop_id
        }
    
    try:
        data = requests.get(url_search_item.format(match_id,newest)).content
        data = json.loads(data)['items']
        res = list(map(get_item_shop_id, data))
        return pd.DataFrame(res)
    except:
        print(f"An exception occurred with match_id {match_id} and newest {newest} ")
    # res = pd.DataFrame(df)
    # return res