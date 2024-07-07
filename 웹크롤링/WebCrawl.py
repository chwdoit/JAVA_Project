from bs4 import BeautifulSoup
import urllib.request
import pandas as pd
import re
Gmarket_stores = list()
Gmarket_url = f'https://www.gmarket.co.kr/n/search?keyword=%ed%82%a4%eb%b3%b4%eb%93%9c&s=8&f=c:300028457'
print(Gmarket_url)
html = urllib.request.urlopen(Gmarket_url)
soup_Gmarket = BeautifulSoup(html, 'html.parser')
product_list = soup_Gmarket.find_all('div', class_='section__module-wrap')
for product in product_list[11].find_all(class_='box__component box__component-itemcard box__component-itemcard--general') :
    name = product.find(class_='text__item').string
    price_components = product.find_all(class_='text text__value')
    if len(price_components) == 1:
        # 할인이 없는 경우
        price_str = price_components[0].string
        price = ''.join(re.findall('\d+', price_str))
    elif len(price_components) > 1:
        # 할인이 있는 경우
        final_price_str = price_components[-1].string
        price = ''.join(re.findall('\d+', final_price_str))
    
    purchase_count = product.find(class_='text').string  # 구매수 추출
    review_count = product.find(class_='text').string  # 리뷰수 추출
    
    # 구매수 추출
    purchase_count_tag = product.find(class_='list-item list-item__pay-count')
    if purchase_count_tag:
        purchase_count = purchase_count_tag.find(class_='text').get_text().strip()
        purchase_count = ''.join(re.findall('\d+', purchase_count))
    else:
        purchase_count = '0'
    
    # 리뷰수 추출
    review_count_tag = product.find(class_='list-item list-item__feedback-count')
    if review_count_tag:
        review_count = review_count_tag.find(class_='text').get_text().strip()
        review_count = ''.join(re.findall('\d+', review_count))
    else:
        review_count = '0'
    Gmarket_stores.append([name] + [price] + [review_count] + [purchase_count])
    
    print(name)
    print(price)
    print(purchase_count)
    print(review_count)

Gmarket_tb1 = pd.DataFrame(data=Gmarket_stores, 
                           columns=('이름','가격','리뷰수','구매수'))
Gmarket_tb1.to_csv('./Gmarket2_table_Noindex.csv',
                   encoding='utf-8', mode='w', index = False)
