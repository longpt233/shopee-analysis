import requests 
import json
import pandas as pd
from tqdm import tqdm
def get_detail_product(item_shop_id,topic):
    #itemid : product id, shopid: shop id
    url_detail_item = 'https://shopee.vn/api/v4/item/get?itemid={}&shopid={}'
    df = []
    for x in tqdm(range(item_shop_id.shape[0])):
        a,b = item_shop_id.values[x]
        try:
            data = requests.get(url_detail_item.format(a,b)).content
            data = json.loads(data)['data']
            data['topic'] = str(topic)
            df.extend([data])
        except:
            print('Crawl website error')
    return df